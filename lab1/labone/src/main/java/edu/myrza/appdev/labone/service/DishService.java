package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.Dish;
import edu.myrza.appdev.labone.domain.DishIngredient;
import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.payload.dish.DishUpdIngData;
import edu.myrza.appdev.labone.payload.dish.DishUpdReqBody;
import edu.myrza.appdev.labone.repository.DishRepository;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import edu.myrza.appdev.labone.util.BadReqSubcodes;
import edu.myrza.appdev.labone.util.EntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    @Transactional
    public void update(Long id, DishUpdReqBody reqBody){

        Dish dishToUpd = dishRepository.findById(id)
                                       .orElseThrow(noSuchEntityExc(id,"Dish"));

        if(notBlank(reqBody.getName()))   dishToUpd.setName(reqBody.getName());
        if(notBlank(reqBody.getPrice()))  dishToUpd.setPrice(reqBody.getPrice());

        Map<String, List<DishUpdIngData>> mapByStatus = reqBody.getIngredients()
                                                                .stream()
                                                                .collect(Collectors.groupingBy(DishUpdIngData::getStatus));

        for (DishUpdIngData ingData : mapByStatus.get("NEW")) addIng(ingData,dishToUpd);
        for (DishUpdIngData ingData : mapByStatus.get("UPD")) updateIng(ingData,dishToUpd);
        for (DishUpdIngData ingData : mapByStatus.get("DEL")) removeIng(ingData,dishToUpd);

    }

    private void updateIng(DishUpdIngData ingData,Dish dishToUpd){

    }

    private void addIng(DishUpdIngData ingData,Dish dishToUpd){

        Long id = ingData.getId();
        if(!notBlank(id)){
            //todo id can't be null
        }

        //todo price below zero
        if(ingData.getAmount() < 0.0)
            throw new EntityException(BadReqSubcodes.PRICE_BELOW_ZERO,id,"Ingredient");

        //todo an ingredient is already added


        //todo move this to IngredientService
        Ingredient ingredient = ingRepository.findById(id)
                                             .orElseThrow(noSuchEntityExc(id,"Ingredient"));

        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setIngredient(ingredient);
        dishIngredient.setDish(dishToUpd);
        dishIngredient.setAmount(ingData.getAmount());

        dishToUpd.getDishIngredients().add(dishIngredient);
    }

    private void removeIng(DishUpdIngData ingData,Dish dishToUpd){
        dishToUpd.getDishIngredients().removeIf(di -> di.getIngredient().getId().equals(ingData.getId()));
    }

    private Supplier<EntityException> noSuchEntityExc(final Long id, final String entityName){
        return () -> new EntityException(BadReqSubcodes.NO_SUCH_ENTITY,id,entityName);
    }

    private static boolean notBlank(String str){
        return str != null && !str.isEmpty();
    }

    private static boolean notBlank(Double d){
        return d != null;
    }

    private static boolean notBlank(Long l){
        return l != null;
    }
}
