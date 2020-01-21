package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Dish;
import edu.myrza.appdev.labone.domain.DishIngredient;
import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.payload.dish.DishCrtIngData;
import edu.myrza.appdev.labone.payload.dish.DishCrtReqBody;
import edu.myrza.appdev.labone.payload.dish.DishUpdReqBody;
import edu.myrza.appdev.labone.repository.DishIngredientRepository;
import edu.myrza.appdev.labone.repository.DishRepository;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    private DishRepository dishRep;
    private DishIngredientRepository dishIngRep;
    private IngredientRepository ingRep;

    @Autowired
    DishController(DishRepository dishRep,
                   IngredientRepository ingRep,
                   DishIngredientRepository dishIngRep)
    {
        this.dishIngRep = dishIngRep;
        this.dishRep = dishRep;
        this.ingRep = ingRep;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DishCrtReqBody reqBody){
        //todo get validator

        //todo save dish
        Dish dish = new Dish();
        dish.setName(reqBody.getName());
        dish.setPrice(reqBody.getPrice());

        //todo handle unique name constraint violation
        final Dish _dish = dishRep.save(dish);

        //todo create collection of DishIngredient objects and save them
        reqBody.getIngredients().stream()
                             .map(ingData ->{
                                 //todo write ingredient service and delegate this to it
                                 Ingredient ing = ingRep.findById(ingData.getId())
                                                         .orElseThrow(IllegalArgumentException::new);

                                 DishIngredient res = new DishIngredient();
                                 res.setAmount(ingData.getAmount());
                                 res.setDish(_dish);
                                 res.setIngredient(ing);

                                 return res;
                             })
                             .forEach(dishIng -> {
                                 //todo handle unique constaint violation exceptions
                                 dishIngRep.save(dishIng);
                             });

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DishUpdReqBody reqBody){

        //todo new name
        //todo new price

        //todo delete ingredients that no longer in the recipe
        //todo check if new ingredients exist in the db
        //todo set correct amounts

        return null;
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
