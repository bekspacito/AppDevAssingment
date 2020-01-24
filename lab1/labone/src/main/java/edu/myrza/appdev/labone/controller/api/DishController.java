package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Dish;
import edu.myrza.appdev.labone.domain.DishIngredient;
import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.payload.dish.DishCrtReqBody;
import edu.myrza.appdev.labone.payload.dish.DishUpdIngData;
import edu.myrza.appdev.labone.payload.dish.DishUpdReqBody;
import edu.myrza.appdev.labone.repository.DishIngredientRepository;
import edu.myrza.appdev.labone.repository.DishRepository;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    private DishRepository dishRepo;
    private DishIngredientRepository dishIngRepo;
    private IngredientRepository ingRepo;

    @Autowired
    DishController(DishRepository dishRepo,
                   IngredientRepository ingRepo,
                   DishIngredientRepository dishIngRepo)
    {
        this.dishIngRepo = dishIngRepo;
        this.dishRepo = dishRepo;
        this.ingRepo = ingRepo;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DishCrtReqBody reqBody){
        //todo get validator

        //todo save dish
        Dish dish = new Dish();
        dish.setName(reqBody.getName());
        dish.setPrice(reqBody.getPrice());

        //todo handle unique name constraint violation
        final Dish _dish = dishRepo.save(dish);

        //todo create collection of DishIngredient objects and save them
        reqBody.getIngredients().stream()
                             .map(ingData ->{
                                 //todo write ingredient service and delegate this to it
                                 Ingredient ing = ingRepo.findById(ingData.getId())
                                                         .orElseThrow(IllegalArgumentException::new);

                                 DishIngredient res = new DishIngredient();
                                 res.setAmount(ingData.getAmount());
                                 res.setDish(_dish);
                                 res.setIngredient(ing);

                                 return res;
                             })
                             .forEach(dishIng -> {
                                 //todo handle unique constaint violation exceptions
                                 dishIngRepo.save(dishIng);
                             });

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DishUpdReqBody reqBody){

        Dish dish = dishRepo.findById(id)
                             .orElseThrow(IllegalArgumentException::new);

        //here is gonna be another query to db.
        Set<DishIngredient> currDishIngs = dish.getDishIngredients();

        String name  = reqBody.getName();
        Double price = reqBody.getPrice();
        Set<DishUpdIngData> newDishIngs = reqBody.getIngredients();

        if(name != null)  dish.setName(name);
        if(price != null) dish.setPrice(price);
        if(newDishIngs != null && !newDishIngs.isEmpty()) {
            //check if all exist
            for (DishUpdIngData ingData : newDishIngs){
                Long ingId = ingData.getId();
                if(ingId == null) {} //todo ing id can't be null/you should set id
                if(!ingRepo.existsById(ingData.getId())) {} //todo ing doen't exist
            }

            for (DishUpdIngData ingData : newDishIngs) {
                if (ingData.getStatus().equals("NEW")) {
                    Ingredient ing = ingRepo.findById(ingData.getId())
                                             .orElseThrow(IllegalArgumentException::new);

                    DishIngredient dishIng = new DishIngredient();
                    dishIng.setIngredient(ing);
                    dishIng.setDish(dish);
                    dishIng.setAmount(ingData.getAmount());

                    currDishIngs.add(dishIng);

                } else if (ingData.getStatus().equals("UPD")) {
                    Long _id = ingData.getId();
                    Double _amount = ingData.getAmount();

                    currDishIngs.stream()
                             .filter(di -> di.getIngredient().getId().equals(_id))
                             .findAny()
                             .ifPresent(di -> di.setAmount(_amount));

                } else if (ingData.getStatus().equals("DEL")) {
                    currDishIngs.removeIf(di -> di.getIngredient().getId().equals(ingData.getId()));
                }
            }
        }

        dishRepo.save(dish);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){ return null; }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){ return null; }

    @GetMapping("/pname/{partname}")
    public ResponseEntity<?> findByPartName(@PathVariable String partname){ return null; }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){ return null; }

}
