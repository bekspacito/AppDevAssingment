package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.payload.ingredient.IngCreateReqBody;
import edu.myrza.appdev.labone.payload.ingredient.IngFindResBody;
import edu.myrza.appdev.labone.payload.ingredient.IngUpdateReqBody;
import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import edu.myrza.appdev.labone.repository.UnitRepository;
import edu.myrza.appdev.labone.util.BadReqSubcodes;
import edu.myrza.appdev.labone.util.BadReqException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {

    private IngredientRepository ingrRepository;
    private UnitRepository       unitRepository;

    @Autowired
    public IngredientController(IngredientRepository ingrRepository,
                                UnitRepository unitRepository)
    {
        this.ingrRepository = ingrRepository;
        this.unitRepository = unitRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody IngCreateReqBody reqBody, Errors errors){

        if(errors.hasErrors())
            return ResponseEntity.badRequest()
                                .body(BadReqSubcodes.getRespBody(BadReqSubcodes.WRONG_INPUT));

        String name  = reqBody.getName();
        Double price = reqBody.getPrice();
        Long unitId  = reqBody.getUnitId();

        try {
            Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new BadReqException(unitId,BadReqSubcodes.NO_SUCH_UNIT.getCode(),"Unit"));

            Ingredient newIng = new Ingredient();
            newIng.setName(name);
            newIng.setPrice(price); //todo check if price equals to or is below zero
            newIng.setUnit(unit);

            ingrRepository.save(newIng);

        }catch (BadReqException ex){
            if(ex.getRespBody().getCode().equals(BadReqSubcodes.NO_SUCH_UNIT.getCode()))
                return ResponseEntity.badRequest()
                                    .body(ex.getRespBody());
        }catch (DataIntegrityViolationException ex){
            if(ex.contains(ConstraintViolationException.class)){
                return ResponseEntity.badRequest()
                        .body(BadReqSubcodes.getRespBody(BadReqSubcodes.UINC_VIOLATION));
            }
        }

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        try{
            ingrRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            //this exception is thrown when we try to delete an entity that doesn't exits
            //do nothing
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody IngUpdateReqBody reqBody){

        String name  = reqBody.getName();
        Double price = reqBody.getPrice();
        Long unitId  = reqBody.getUnitId();

        try {
            if(ingrRepository.existsIngredientByName(reqBody.getName()))
                throw new BadReqException(id,BadReqSubcodes.UINC_VIOLATION.getCode(),"Ingredient");

            Ingredient ingToUpd = ingrRepository.findById(id).orElseThrow(() -> new BadReqException(id,BadReqSubcodes.NO_SUCH_INGR.getCode(),"Ingredient"));

            if(name != null) ingToUpd.setName(name);
            if(price != null) ingToUpd.setPrice(price); //todo check if price equals to or is below zero
            if(unitId != null) {
                Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new BadReqException(unitId,BadReqSubcodes.NO_SUCH_UNIT.getCode(),"Unit"));
                ingToUpd.setUnit(unit);
            }

            ingrRepository.save(ingToUpd);

        }catch (BadReqException ex){

            return ResponseEntity.badRequest().body(ex.getRespBody());

        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        IngFindResBody resBody = null;

        try {
            //find and retrieve an ingredient
            Ingredient ing = ingrRepository.findById(id).orElseThrow(() -> new BadReqException(id,BadReqSubcodes.NO_SUCH_INGR.getCode(),"Ingredient"));

            resBody = convert(ing);

        }catch (BadReqException ex){
            if(ex.getRespBody().getCode().equals(BadReqSubcodes.NO_SUCH_INGR.getCode()))
                    return ResponseEntity.badRequest()
                                    .body(BadReqSubcodes.getRespBody(BadReqSubcodes.NO_SUCH_INGR));
        }

        return ResponseEntity.ok(resBody);
    }

    @GetMapping("/pname/{partname}")
    public ResponseEntity<?> findByPartName(@PathVariable String partname){
        final String key = "%" + partname + "%";

        List<IngFindResBody> resp = ingrRepository.findByNameLike(key).stream()
                                                    .map(IngredientController::convert)
                                                    .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<IngFindResBody> resp = StreamSupport.stream(ingrRepository.findAll().spliterator(),false)
                                                    .map(IngredientController::convert)
                                                    .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    private static IngFindResBody convert(Ingredient ing){

        IngFindResBody result = new IngFindResBody();

        Unit unit = ing.getUnit();

        //get unit ready
        UnitRespBody unitRespBody = new UnitRespBody();
        unitRespBody.setId(unit.getId());
        unitRespBody.setName(unit.getName());

        result.setId(ing.getId());
        result.setName(ing.getName());
        result.setPrice(ing.getPrice());
        result.setUnit(unitRespBody);

        return result;
    }

}
