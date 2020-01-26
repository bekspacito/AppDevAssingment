package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.Dish;
import edu.myrza.appdev.labone.domain.DishIngredient;
import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.payload.dish.DishCrtIngData;
import edu.myrza.appdev.labone.payload.dish.DishCrtReqBody;
import edu.myrza.appdev.labone.payload.dish.DishUpdIngData;
import edu.myrza.appdev.labone.payload.dish.DishUpdReqBody;
import edu.myrza.appdev.labone.repository.DishRepository;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import edu.myrza.appdev.labone.util.BadReqException;
import edu.myrza.appdev.labone.util.BadReqSubcodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final IngredientRepository ingRepository;
    private final Map<String, BiConsumer<DishUpdIngData,Dish>> statusToHandler;

    @Autowired
    public DishService(DishRepository dishRepository,
                       IngredientRepository ingredientRepository)
    {
        this.dishRepository = dishRepository;
        this.ingRepository = ingredientRepository;
        statusToHandler = new HashMap<>();
        statusToHandler.put("NEW",this::addIng);
        statusToHandler.put("DEL",this::removeIng);
        statusToHandler.put("UPD",this::updateIng);
    }

    @Transactional
    public void create(DishCrtReqBody reqBody){

        Dish dish = new Dish();
        dish.setName(reqBody.getName());
        dish.setPrice(reqBody.getPrice());

        reqBody.getIngredients().stream()
                        .map(di -> mapToDishIng(di,dish))
                        .forEach(di -> dish.getDishIngredients().add(di));

        dishRepository.save(dish);

    }

    private DishIngredient mapToDishIng(DishCrtIngData ingData, Dish dish){
        Ingredient ingredient = ingRepository.findById(ingData.getId())
                                             .orElseThrow(noSuchEntityExc(ingData.getId(),"Ingredient"));

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setDish(dish);
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setAmount(ingData.getAmount());

        return dishIngredient;
    }

    @Transactional
    public void update(Long id, DishUpdReqBody reqBody){

        Dish dishToUpd = dishRepository.findById(id)
                                       .orElseThrow(noSuchEntityExc(id,"Dish"));

        if(notBlank(reqBody.getName()))   dishToUpd.setName(reqBody.getName());
        if(reqBody.getPrice() != null)  dishToUpd.setPrice(reqBody.getPrice());

        Map<String, List<DishUpdIngData>> mapByStatus = reqBody.getIngredients()
                                                                .stream()
                                                                .collect(Collectors.groupingBy(DishUpdIngData::getStatus));

        for(String status : mapByStatus.keySet()){
            BiConsumer<DishUpdIngData,Dish> handler = statusToHandler.get(status);
            for(DishUpdIngData ingData : mapByStatus.get(status))
                handler.accept(ingData,dishToUpd);
        }

    }

    private void updateIng(DishUpdIngData ingData,Dish dishToUpd){
        final Long id = ingData.getId();

        if(id == null)
            throw new BadReqException(dishToUpd.getName(),BadReqSubcodes.IS_NULL.getCode(),"Ingredient");

        if(ingData.getAmount() == null || ingData.getAmount() < 0.0)
            throw new BadReqException(id,BadReqSubcodes.WRONG_AMOUNT.getCode(),"Ingredient");

        dishToUpd.getDishIngredients().stream()
                              .filter(di -> di.getIngredient().getId().equals(id))
                              .findAny()
                              .ifPresent(di -> {
                                  di.setAmount(ingData.getAmount());
                              });
    }

    private void addIng(DishUpdIngData ingData,Dish dishToUpd){

        final Long id = ingData.getId();

        if(id == null)
            throw new BadReqException(dishToUpd.getName(),BadReqSubcodes.IS_NULL.getCode(),"Ingredient");

        if(ingData.getAmount() == null || ingData.getAmount() < 0.0)
            throw new BadReqException(id,BadReqSubcodes.WRONG_AMOUNT.getCode(),"Ingredient");

        //todo move this to IngredientService
        Ingredient ingredient = ingRepository.findById(id)
                                             .orElseThrow(noSuchEntityExc(id,"Ingredient"));

        boolean isAlreadyAdded = dishToUpd.getDishIngredients().stream()
                                            .anyMatch(di -> di.getIngredient().getId().equals(id));
        if(isAlreadyAdded)
            throw new BadReqException(id,BadReqSubcodes.ALREADY_ADDED.getCode(),"Ingredient");

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setDish(dishToUpd);
        dishIngredient.setAmount(ingData.getAmount());

        dishToUpd.getDishIngredients().add(dishIngredient);
    }

    private void removeIng(DishUpdIngData ingData,Dish dishToUpd){
        Set<DishIngredient> dishIngs = dishToUpd.getDishIngredients();

        if(dishIngs.size() <= 1)
            throw new BadReqException(dishToUpd.getId(),BadReqSubcodes.DELL_ALL_INGS.getCode(),"Dish");

        dishIngs.removeIf(di -> di.getIngredient().getId().equals(ingData.getId()));
    }

    private static Supplier<BadReqException> noSuchEntityExc(Long id,String entityName){
        return () -> new BadReqException(id,BadReqSubcodes.NO_SUCH_ENTITY.getCode(),entityName);
    }

    private static boolean notBlank(String str){
        return str != null && !str.isEmpty();
    }

}
