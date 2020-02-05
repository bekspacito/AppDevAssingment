package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.api.dish.create.DishCreateError;
import edu.myrza.appdev.labone.error.api.dish.update.DishUpdateError;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.dish.FindDishRespBody;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;
import edu.myrza.appdev.labone.service.DishService;
import edu.myrza.appdev.labone.exception.BadReqException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Payload;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            return ResponseEntity.badRequest().body(errors.get(0));


        try {
            dishService.update(id,reqBody);
        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }catch (DataIntegrityViolationException ex){
            if(ConstraintViolationException.class.isAssignableFrom(ex.getCause().getClass())) {
                ConstraintViolationException cve = (ConstraintViolationException) ex.getCause();
                String cveName = cve.getConstraintName();

                //is thrown when duplicate ingredients come
                if (cveName.contains("ING_DISH_UNIQUE")) {
                    BadReqResponseBody body = reqBody.getIngredients().stream()
                            .collect(Collectors.groupingBy(UpdateReqBody.IngredientData::getId, Collectors.counting()))
                            .entrySet().stream()
                            .filter(es -> es.getValue() > 1)
                            .findFirst()
                            .map(di -> new BadReqResponseBody.Builder(BadReqCodes.DUPLICATE_ENTITIES)
                                            .identifier(di.getKey())
                                            .build()
                            )
                            //when we try to add an ingredient that is already in list of ingredients
                            //todo we cannot say what ingredient is exactly has been tried to be added
                            .orElseGet(() -> new BadReqResponseBody.Builder(BadReqCodes.ALREADY_ADDED)
                                            .build()
                            );

                    return ResponseEntity.badRequest().body(body);
                }
                if (cveName.contains("DISH_UNIQUE_NAME")) {
                    BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.UNIQUE_ENTITY)
                            .identifier(reqBody.getName())
                            .build();

                    return ResponseEntity.badRequest().body(body);
                }
            }

        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        dishService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        FindDishRespBody resBody = null;

        try {
            resBody = dishService.findById(id);
        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok(resBody);
    }

    @GetMapping("/pname/{partname}")
    public ResponseEntity<?> findByPartName(@PathVariable String partname){
        List<FindDishRespBody> res = dishService.findByPartname(partname);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<FindDishRespBody> res = dishService.findAll();

        return ResponseEntity.ok(res);
    }

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
