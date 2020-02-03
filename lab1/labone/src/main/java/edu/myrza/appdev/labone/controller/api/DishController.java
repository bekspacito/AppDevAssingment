package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.error.api.dish.create.DishCreateError;
import edu.myrza.appdev.labone.error.api.dish.update.DishUpdateError;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;
import edu.myrza.appdev.labone.service.DishService;
import edu.myrza.appdev.labone.exception.BadReqException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Payload;
import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/dish")
public class DishController {

    private Validator validator;
    private DishService dishService;

    @Autowired
    DishController(DishService dishService,
                   Validator validator)
    {
        this.dishService = dishService;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateReqBody reqBody){

         List errors = validator.validate(reqBody)
                             .stream()
                             .flatMap(cv -> cv.getConstraintDescriptor().getPayload().stream())
                             .map(p -> processError(p,reqBody))
                             .collect(Collectors.toList());

         if(errors.size() > 0)
             return ResponseEntity.badRequest().body(errors);

        try{
            dishService.create(reqBody);
        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateReqBody reqBody){

        List errors = validator.validate(reqBody)
                .stream()
                .flatMap(cv -> cv.getConstraintDescriptor().getPayload().stream())
                .map(p -> processError(p,reqBody))
                .collect(Collectors.toList());

        if(errors.size() > 0)
            return ResponseEntity.badRequest().body(errors);


//        try {
//            dishService.update(id,reqBody);
//        }catch (BadReqException ex){
//            return ResponseEntity.badRequest().body(ex.getRespBody());
//        }

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

    private static Object processError(Class<? extends Payload> p,CreateReqBody reqBody){
        try {
            if (DishCreateError.class.isAssignableFrom(p)) {
                DishCreateError handler = (DishCreateError) p.newInstance();
                return handler.onError(reqBody);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    private static Object processError(Class<? extends Payload> p,UpdateReqBody reqBody){
        try {
            if (DishUpdateError.class.isAssignableFrom(p)) {
                DishUpdateError handler = (DishUpdateError) p.newInstance();
                return handler.onError(reqBody);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

}
