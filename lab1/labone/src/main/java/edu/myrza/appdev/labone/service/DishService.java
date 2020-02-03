package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.Dish;
import edu.myrza.appdev.labone.domain.DishIngredient;
import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;
import edu.myrza.appdev.labone.repository.DishRepository;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final IngredientRepository ingRepository;

    @Autowired
    public DishService(DishRepository dishRepository,
                       IngredientRepository ingredientRepository)
    {
        this.dishRepository = dishRepository;
        this.ingRepository = ingredientRepository;
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

        }catch (Exception ex){
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
        Ingredient ingredient = ingRepository.findById(ingData.getId())
                                             .orElseThrow(noSuchEntityExc(ingData.getId(),"Ingredient"));

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setDish(dish);
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setAmount(ingData.getAmount().doubleValue());

        return dishIngredient;
    }



//    @Transactional
//    public void update(Long id, UpdateReqBody reqBody){
//
//        Dish dishToUpd = dishRepository.findById(id)
//                                       .orElseThrow(noSuchEntityExc(id,"Dish"));
//
//        dishToUpd.setName(reqBody.getName());
//        dishToUpd.setPrice(reqBody.getPrice());
//
//        Map<String, List<DishUpdIngData>> mapByStatus = reqBody.getIngredients()
//                                                                .stream()
//                                                                .collect(Collectors.groupingBy(DishUpdIngData::getStatus));
//
//        for(String status : mapByStatus.keySet()){
//            BiConsumer<DishUpdIngData,Dish> handler = statusToHandler.get(status);
//            for(DishUpdIngData ingData : mapByStatus.get(status))
//                handler.accept(ingData,dishToUpd);
//        }
//
//    }
//
//    private void updateIng(DishUpdIngData ingData,Dish dishToUpd){
//        final Long id = ingData.getId();
//
//        dishToUpd.getDishIngredients().stream()
//                              .filter(di -> di.getIngredient().getId().equals(id))
//                              .findAny()
//                              .ifPresent(di -> {
//                                  di.setAmount(ingData.getAmount().doubleValue());
//                              });
//    }
//
//    private void addIng(DishUpdIngData ingData,Dish dishToUpd){
//
//        final Long id = ingData.getId();
//
//        //todo move this to IngredientService
//        Ingredient ingredient = ingRepository.findById(id)
//                                             .orElseThrow(noSuchEntityExc(id,"Ingredient"));
//
//        DishIngredient dishIngredient = new DishIngredient();
//        dishIngredient.setIngredient(ingredient);
//        dishIngredient.setDish(dishToUpd);
//        dishIngredient.setAmount(ingData.getAmount().doubleValue());
//
//        dishToUpd.getDishIngredients().add(dishIngredient);
//    }
//
//    private void removeIng(DishUpdIngData ingData,Dish dishToUpd){
//        Set<DishIngredient> dishIngs = dishToUpd.getDishIngredients();
//
//        if(dishIngs.size() <= 1){
//            BadReqResponseBody resp = new BadReqResponseBody.Builder(BadReqCodes.NO_ING_DISH)
//                                                            .identifier(dishToUpd.getId())
//                                                            .build();
//            throw new BadReqException(resp);
//        }
//
//        dishIngs.removeIf(di -> di.getIngredient().getId().equals(ingData.getId()));
//    }
//
    private static Supplier<BadReqException> noSuchEntityExc(Long id, String entity){
        return () -> {
            BadReqResponseBody resp = new BadReqResponseBody.Builder(BadReqCodes.NO_SUCH_ENTITY)
                                                    .entity(entity)
                                                    .identifier(id)
                                                    .build();
            return new BadReqException(resp);
        };
    }
//
//    private static boolean notBlank(String str){
//        return str != null && !str.isEmpty();
//    }

}
