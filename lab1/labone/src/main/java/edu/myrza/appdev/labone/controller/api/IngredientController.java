package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.api.ingredient.create.IngredientCreateError;
import edu.myrza.appdev.labone.error.api.ingredient.update.IngredientUpdateError;
import edu.myrza.appdev.labone.payload.ingredient.CreateReqBody;
import edu.myrza.appdev.labone.payload.ingredient.FindRespBody;
import edu.myrza.appdev.labone.payload.ingredient.UpdateReqBody;
import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.service.IngredientService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Payload;
import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {

    private Validator            validator;
    private IngredientService    ingService;

    @Autowired
    public IngredientController(Validator validator,
                                IngredientService ingService)
    {
        this.validator = validator;
        this.ingService = ingService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateReqBody reqBody){

        //validation
        List<Object> errors = validator.validate(reqBody)
                 .stream()
                 .flatMap(cv -> cv.getConstraintDescriptor().getPayload().stream())
                 .map(p -> processError(p,reqBody))
                 .collect(Collectors.toList());

        if(errors.size() > 0)
            return ResponseEntity.badRequest().body(errors.get(0));

        try {

            ingService.save(reqBody);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }catch (ConstraintViolationException cvex){
               if(cvex.getConstraintName().contains("ING_UNIQUE_NAME")){
                    BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.UINC_VIOLATION)
                                                                .identifier(reqBody.getName())
                                                                .build();
                    return ResponseEntity.badRequest().body(body);
               }
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){

        try{
            ingService.delete(id);
        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateReqBody reqBody){

        //validation
        List<Object> errors = validator.validate(reqBody)
                .stream()
                .flatMap(cv -> cv.getConstraintDescriptor().getPayload().stream())
                .map(p -> processError(p,reqBody))
                .collect(Collectors.toList());

        if(errors.size() > 0)
            return ResponseEntity.badRequest().body(errors.get(0));

        try {

            ingService.update(id,reqBody);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }catch (ConstraintViolationException cvex){
            if(cvex.getConstraintName().contains("ING_UNIQUE_NAME")){
                BadReqResponseBody resp = new BadReqResponseBody.Builder(BadReqCodes.UINC_VIOLATION)
                        .identifier(reqBody.getName())
                        .build();
                return ResponseEntity.badRequest().body(resp);
            }
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        FindRespBody resBody = null;

        try {
            resBody = convert(ingService.findById(id));
        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok(resBody);
    }

    @GetMapping("/pname/{partname}")
    public ResponseEntity<?> findByPartName(@PathVariable String partname){
        List<FindRespBody> resp = StreamSupport.stream(ingService.findByPartName(partname).spliterator(),false)
                                                    .map(IngredientController::convert)
                                                    .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<FindRespBody> resp = StreamSupport.stream(ingService.findAll().spliterator(),false)
                                                    .map(IngredientController::convert)
                                                    .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    private static Object processError(Class<? extends Payload> p, CreateReqBody reqBody){
        try {
            if (IngredientCreateError.class.isAssignableFrom(p)) {
                IngredientCreateError handler = (IngredientCreateError) p.newInstance();
                return handler.onError(reqBody);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        throw new RuntimeException("No handler is found...");
    }

    private static Object processError(Class<? extends Payload> p,UpdateReqBody reqBody){
        try {
            if (IngredientUpdateError.class.isAssignableFrom(p)) {
                IngredientUpdateError handler = (IngredientUpdateError) p.newInstance();
                return handler.onError(reqBody);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        throw new RuntimeException("No handler is found...");
    }

    private static FindRespBody convert(Ingredient ing){

        FindRespBody result = new FindRespBody();

        Unit unit = ing.getUnit();

        //get unit ready
        UnitRespBody unitRespBody = new UnitRespBody();
        unitRespBody.setId(unit.getId());
        unitRespBody.setName(unit.getName());

        //get ingredient ready
        result.setId(ing.getId());
        result.setName(ing.getName());
        result.setPrice(ing.getPrice());
        result.setUnit(unitRespBody);

        return result;
    }

}
