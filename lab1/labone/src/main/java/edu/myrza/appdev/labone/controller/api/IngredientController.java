package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.payload.ingredient.IngReqBody;
import edu.myrza.appdev.labone.payload.ingredient.IngFindResBody;
import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import edu.myrza.appdev.labone.repository.UnitRepository;
import edu.myrza.appdev.labone.util.BadReqSubcodes;
import edu.myrza.appdev.labone.util.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> create(@RequestBody IngReqBody reqBody){

        String name  = reqBody.getName();
        Double price = reqBody.getPrice();
        Long unitId  = reqBody.getUnitId();

        try {
            Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new BadRequestException(BadReqSubcodes.NO_SUCH_UNIT));

            Ingredient newIng = new Ingredient();
            newIng.setName(name);
            newIng.setPrice(price); //todo check if price equals to or is below zero
            newIng.setUnit(unit);

            ingrRepository.save(newIng);

        }catch (BadRequestException ex){
            if(ex.getSubcode().equals(BadReqSubcodes.NO_SUCH_UNIT))
                return ResponseEntity.badRequest()
                                    .body(BadReqSubcodes.getRespBody(BadReqSubcodes.NO_SUCH_UNIT));
        }catch (DataIntegrityViolationException ex){
            if(ex.contains(ConstraintViolationException.class)){
                return ResponseEntity.badRequest()
                        .body(BadReqSubcodes.getRespBody(BadReqSubcodes.UINC_VIOLATION));
            }
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
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
    public ResponseEntity<?> update(@RequestBody IngReqBody reqBody){
        // todo name unique constraint violation
        // todo someone could delete an entity while we are updating it
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        IngFindResBody resBody = null;

        try {
            //find and retrieve an ingredient
            Ingredient ing = ingrRepository.findById(id).orElseThrow(() -> new BadRequestException(BadReqSubcodes.NO_SUCH_INGR));

            resBody = convert(ing);

        }catch (BadRequestException ex){
            if(ex.getSubcode().equals(BadReqSubcodes.NO_SUCH_INGR))
                    return ResponseEntity.badRequest()
                                    .body(BadReqSubcodes.getRespBody(BadReqSubcodes.NO_SUCH_INGR));
        }

        return ResponseEntity.ok(resBody);
    }

    @GetMapping("/pname")
    public ResponseEntity<?> findByPartName(@RequestParam String name){
        final String key = "%" + name + "%";

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
