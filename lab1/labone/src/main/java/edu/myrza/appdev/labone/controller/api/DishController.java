package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.payload.dish.DishCrtReqBody;
import edu.myrza.appdev.labone.payload.dish.DishUpdReqBody;
import edu.myrza.appdev.labone.service.DishService;
import edu.myrza.appdev.labone.exception.BadReqException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dish")
public class DishController {

    private DishService dishService;

    @Autowired
    DishController(DishService dishService)
    {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DishCrtReqBody reqBody){

        try{
            dishService.create(reqBody);
        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DishUpdReqBody reqBody){

        try {
            dishService.update(id,reqBody);
        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

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
