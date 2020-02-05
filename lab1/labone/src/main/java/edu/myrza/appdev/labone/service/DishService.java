package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.Dish;
import edu.myrza.appdev.labone.domain.DishIngredient;
import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.dish.FindDishRespBody;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;
import edu.myrza.appdev.labone.payload.ingredient.FindIngRespBody;
import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import edu.myrza.appdev.labone.repository.DishRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final IngredientService ingService;

    @Autowired
    public DishService(DishRepository dishRepository,
                       IngredientService ingService)
    {
        this.dishRepository = dishRepository;
        this.ingService = ingService;
    }

    public void create(CreateReqBody reqBody){

        try {
            Dish newDish = new Dish();
            newDish.setName(reqBody.getName());
            newDish.setPrice(reqBody.getPrice().doubleValue());

            Set<DishIngredient> dishIngs = reqBody.getIngredients().stream()
                            .map(ingData -> convert(ingData, newDish))
                            .collect(Collectors.toSet());

            newDish.setDishIngredients(dishIngs);

            dishRepository.save(newDish);

        }catch (DataIntegrityViolationException ex){
                if(ConstraintViolationException.class.isAssignableFrom(ex.getCause().getClass())) {
                    ConstraintViolationException cve = (ConstraintViolationException) ex.getCause();
                    String cveName = cve.getConstraintName();

                    //is thrown when duplicate ingredients come
                    if (cveName.contains("ING_DISH_UNIQUE")) {
                        Map.Entry<Long, Long> dupIng = reqBody.getIngredients().stream()
                                .collect(Collectors.groupingBy(CreateReqBody.IngredientData::getId, Collectors.counting()))
                                .entrySet().stream()
                                .filter(es -> es.getValue() > 1)
                                .findFirst()
                                .get();

                        BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.DUPLICATE_ENTITIES)
                                .identifier(dupIng.getKey())
                                .build();
                        throw new BadReqException(body);
                    }
                    if (cveName.contains("DISH_UNIQUE_NAME")) {
                        BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.UNIQUE_ENTITY)
                                .identifier(reqBody.getName())
                                .build();
                        throw new BadReqException(body);
                    }
                }

        }

    }

    private DishIngredient convert(CreateReqBody.IngredientData ingData, Dish dish){
        Ingredient ingredient = ingService.findById(ingData.getId());

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setDish(dish);
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setAmount(ingData.getAmount().doubleValue());

        return dishIngredient;
    }

    @Transactional //чтобы не было пересохранения
    public void update(Long id, UpdateReqBody reqBody){

        Dish dishToUpd = dishRepository.findById(id).orElseThrow(noSuchDish(id));

        reqBody.getName().ifPresent(dishToUpd::setName);
        reqBody.getPrice().map(BigDecimal::doubleValue).ifPresent(dishToUpd::setPrice);

        Set<UpdateReqBody.IngredientData> ings = reqBody.getIngredients();

        for (UpdateReqBody.IngredientData data : ings) {
            switch (data.getStatus()) {
                case "NEW":
                    addIng(data, dishToUpd);
                    break;
                case "UPD":
                    updateIng(data, dishToUpd);
                    break;
                case "DEL":
                    removeIng(data, dishToUpd);
                    break;
                default:
                    System.out.println("STATUS ISNT SUPPORTED");
            }
        }
    }

    private void updateIng(UpdateReqBody.IngredientData ingData, Dish dishToUpd){
        final Long id = ingData.getId();

        dishToUpd.getDishIngredients().stream()
                              .filter(di -> di.getIngredient().getId().equals(id))
                              .findAny()
                              .ifPresent(di -> {
                                  di.setAmount(ingData.getAmount().doubleValue());
                              });
    }

    private void addIng(UpdateReqBody.IngredientData ingData,Dish dishToUpd){

        Ingredient ingredient = ingService.findById(ingData.getId());

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setDish(dishToUpd);
        dishIngredient.setAmount(ingData.getAmount().doubleValue());

        dishToUpd.getDishIngredients().add(dishIngredient);
    }

    private void removeIng(UpdateReqBody.IngredientData ingData,Dish dishToUpd){
        Set<DishIngredient> dishIngs = dishToUpd.getDishIngredients();

        if(dishIngs.size() <= 1){
            BadReqResponseBody resp = new BadReqResponseBody.Builder(BadReqCodes.EMPTY_ING_LIST_DISH)
                    .identifier(dishToUpd.getId())
                    .build();

            throw  new BadReqException(resp);
        }

        dishIngs.removeIf(di -> di.getIngredient().getId().equals(ingData.getId()));
    }

    public void delete(Long id){
        try {
            dishRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            //this exception is thrown when we try to delete an entity that doesn't exits
            //do nothing
        }
    }

    public FindDishRespBody findById(Long dishId){
        Dish dish = dishRepository.findById(dishId).orElseThrow(noSuchDish(dishId));

        return convert(dish);
    }


    public List<FindDishRespBody> findAll(){
        return StreamSupport.stream(dishRepository.findAll().spliterator(),false)
                                .map(this::convert)
                                .collect(Collectors.toList());
    }


    public List<FindDishRespBody> findByPartname(String partname){
        String key = "%" + partname + "%";

        return dishRepository.findByNameLike(key)
                            .stream()
                            .map(this::convert)
                            .collect(Collectors.toList());
    }

    @Transactional
    protected FindDishRespBody convert(Dish dish){

        List<FindDishRespBody.IngredientData> ingredients = dish.getDishIngredients().stream()
                .map(this::convert)
                .collect(Collectors.toList());

        FindDishRespBody respBody = new FindDishRespBody();

        respBody.setId(dish.getId());
        respBody.setName(dish.getName());
        respBody.setPrice(dish.getPrice());
        respBody.setIngredients(ingredients);

        return respBody;

    }

    private FindDishRespBody.IngredientData convert(DishIngredient dishIng){

        Ingredient ingredient = dishIng.getIngredient();
        Unit unit = ingredient.getUnit();

        //prepare unit data
        UnitRespBody unitBody = new UnitRespBody();
        unitBody.setId(unit.getId());
        unitBody.setName(unit.getName());

        //prepare ingredient data
        FindIngRespBody ingBody = new FindIngRespBody();

        ingBody.setId(ingredient.getId());
        ingBody.setName(ingredient.getName());
        ingBody.setPrice(ingredient.getPrice());
        ingBody.setUnit(unitBody);

        //prepare ingredient data relative to given dish(set amount etc)
        FindDishRespBody.IngredientData ingData = new FindDishRespBody.IngredientData();
        ingData.setAmount(dishIng.getAmount());
        ingData.setIngredient(ingBody);

        return ingData;
    }

    private Supplier<BadReqException> noSuchDish(Long id){
        BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.NO_SUCH_DISH)
                .identifier(id)
                .build();

        return () -> new BadReqException(body);
    }

}
