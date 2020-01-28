package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.FieldErrorCodes;
import edu.myrza.appdev.labone.payload.dish.CreateReqBody;
import edu.myrza.appdev.labone.payload.dish.UpdateReqBody;
import edu.myrza.appdev.labone.service.DishService;
import edu.myrza.appdev.labone.exception.BadReqException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.Annotation;
import java.util.Set;


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


        //validate name
        for(ConstraintViolation<CreateReqBody> cv : validator.validateProperty(reqBody,"name")){
            Class<?> constraint = cv.getConstraintDescriptor().getAnnotation().annotationType();
            if(constraint.equals(NotBlank.class)){
                BadReqResponseBody respBody = new BadReqResponseBody.Builder(BadReqCodes.FIELD_ERROR)
                                                                    .fieldError(FieldErrorCodes.DISH_NAME_REQUIRED)
                                                                    .build();

                return ResponseEntity.badRequest().body(respBody);
            }
        }



//        try{
//            //dishService.create(reqBody);
//        }catch (BadReqException ex){
//            return ResponseEntity.badRequest().body(ex.getRespBody());
//        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateReqBody reqBody){

        try {
            //dishService.update(id,reqBody);
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
