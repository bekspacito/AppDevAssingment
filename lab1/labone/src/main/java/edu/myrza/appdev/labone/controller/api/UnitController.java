package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.error.api.unit.create.UnitCreateError;
import edu.myrza.appdev.labone.error.api.unit.update.UnitUpdateError;
import edu.myrza.appdev.labone.payload.unit.CreateReqBody;
import edu.myrza.appdev.labone.payload.unit.UpdateReqBody;
import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import edu.myrza.appdev.labone.repository.UnitRepository;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.service.UnitService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Payload;
import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/unit")
public class UnitController {

    private final Validator validator;
    private final UnitService unitService;

    @Autowired
    public UnitController(
            Validator validator,
            UnitService unitService)
    {
        this.validator = validator;
        this.unitService = unitService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateReqBody reqBody){

        List<Object> errors = validator.validate(reqBody)
                                 .stream()
                                 .flatMap(cv -> cv.getConstraintDescriptor().getPayload().stream())
                                 .map(p -> processError(p,reqBody))
                                 .collect(Collectors.toList());

        if(errors.size() > 0)
            return ResponseEntity.badRequest().body(errors.get(0));

        try {

            unitService.create(reqBody);

        }catch (ConstraintViolationException cvex){
            if(cvex.getConstraintName().contains("UNIT_UNIQUE_NAME")){
                BadReqResponseBody resp = new BadReqResponseBody.Builder(BadReqCodes.UUNC_VIOLATION)
                                                                .identifier(reqBody.getName())
                                                                .build();
                return ResponseEntity.badRequest().body(resp);
            }
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        try{
            unitService.delete(id);
        }catch (ConstraintViolationException cvex){
                //We try to delete a unit that is used by ingredients
                if(cvex.getConstraintName().contains("FK_ING_UNIT")){
                    BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.UNIT_IS_IN_USE)
                                                                .identifier(id)
                                                                .build();

                    return ResponseEntity.badRequest().body(body);
                }
        }

        return ResponseEntity.ok().build();
    }

    //Now no one can delete an entity that we are trying to update here.
    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody UpdateReqBody reqBody){

        List<Object> errors = validator.validate(reqBody)
                                    .stream()
                                    .flatMap(cv -> cv.getConstraintDescriptor().getPayload().stream())
                                    .map(p -> processError(p,reqBody))
                                    .collect(Collectors.toList());

        if(errors.size() > 0)
            return ResponseEntity.badRequest().body(errors.get(0));

        try {

            unitService.update(id,reqBody);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }catch (ConstraintViolationException cvex){
                //We try to set a name that is already used to name other unit
                if(cvex.getConstraintName().contains("UNIT_UNIQUE_NAME")){
                    BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.UUNC_VIOLATION)
                                                            .identifier(id)
                                                            .build();

                    return ResponseEntity.badRequest().body(body);
                }
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        try {
            Unit unit = unitService.findById(id);

            UnitRespBody respBody = new UnitRespBody();
            respBody.setName(unit.getName());
            respBody.setId(unit.getId());

            return ResponseEntity.ok(respBody);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }
    }

    @GetMapping("/pname/{partname}")
    public ResponseEntity<?> findAllByPartName(@PathVariable String partname){
        List<UnitRespBody> resp = unitService.findAllByPartName(partname).stream()
                                                 .map(unit -> new UnitRespBody(unit.getId(),unit.getName()))
                                                 .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){

        List<UnitRespBody> resp = StreamSupport.stream(unitService.findAll().spliterator(),false)
                .map(u -> new UnitRespBody(u.getId(),u.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    private static Object processError(Class<? extends Payload> p,CreateReqBody reqBody){
        try {
            if (UnitCreateError.class.isAssignableFrom(p)) {
                UnitCreateError handler = (UnitCreateError) p.newInstance();
                return handler.onError(reqBody);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        throw new RuntimeException("No handler is found...");
    }

    private static Object processError(Class<? extends Payload> p,UpdateReqBody reqBody){
        try {
            if (UnitUpdateError.class.isAssignableFrom(p)) {
                UnitUpdateError handler = (UnitUpdateError) p.newInstance();
                return handler.onError(reqBody);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        throw new RuntimeException("No handler is found...");
    }

}
