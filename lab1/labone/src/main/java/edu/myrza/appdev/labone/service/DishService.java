package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.Dish;
import edu.myrza.appdev.labone.domain.DishIngredient;
import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.payload.dish.DishCrtIngData;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.dish.DishUpdIngData;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;
import edu.myrza.appdev.labone.repository.DishRepository;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.error.BadReqCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DishService {

//    private final DishRepository dishRepository;
//    private final IngredientRepository ingRepository;
//    private final Map<String, BiConsumer<DishUpdIngData,Dish>> statusToHandler;
//
//    @Autowired
//    public DishService(DishRepository dishRepository,
//                       IngredientRepository ingredientRepository)
//    {
//        this.dishRepository = dishRepository;
//        this.ingRepository = ingredientRepository;
//
//        statusToHandler = new HashMap<>();
//        statusToHandler.put("NEW",this::addIng);
//        statusToHandler.put("DEL",this::removeIng);
//        statusToHandler.put("UPD",this::updateIng);
//    }
//
//    public void create(CreateReqBody reqBody){
//
//        Dish newDish = new Dish();
//        newDish.setName(reqBody.getName());
//        newDish.setPrice(reqBody.getPrice().doubleValue());
//
//        reqBody.getIngredients().stream()
//                                .map(ingData -> mapToDishIng(ingData,newDish))
//                                .forEach(di -> newDish.getDishIngredients().add(di));
//
//        dishRepository.save(newDish);
//
//    }
//
//    private DishIngredient mapToDishIng(DishCrtIngData ingData, Dish dish){
//        Ingredient ingredient = ingRepository.findById(ingData.getId())
//                                             .orElseThrow(noSuchEntityExc(ingData.getId(),"Ingredient"));
//
//        DishIngredient dishIngredient = new DishIngredient();
//        dishIngredient.setDish(dish);
//        dishIngredient.setIngredient(ingredient);
//        dishIngredient.setAmount(ingData.getAmount().doubleValue());
//
//        return dishIngredient;
//    }
//
//    @Transactional
//    public void update(Long id, UpdateReqBody reqBody){
//
//        Dish dishToUpd = dishRepository.findById(id)
//                                       .orElseThrow(noSuchEntityExc(id,"Dish"));
//
//        if(notBlank(reqBody.getName()))   dishToUpd.setName(reqBody.getName());
//        if(reqBody.getPrice() != null)  dishToUpd.setPrice(reqBody.getPrice());
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
//    private static Supplier<BadReqException> noSuchEntityExc(Long id,String entity){
//        return () -> {
//            BadReqResponseBody resp = new BadReqResponseBody.Builder(BadReqCodes.NO_SUCH_ENTITY)
//                                                    .entity(entity)
//                                                    .identifier(id)
//                                                    .build();
//            return new BadReqException(resp);
//        };
//    }
//
//    private static boolean notBlank(String str){
//        return str != null && !str.isEmpty();
//    }

}
