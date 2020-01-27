package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.payload.unit.UnitCreateReqBody;
import edu.myrza.appdev.labone.payload.unit.UnitUpdateReqBody;
import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import edu.myrza.appdev.labone.repository.UnitRepository;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.exception.BadReqException;
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
@RequestMapping("/api/unit")
public class UnitController {

    private UnitRepository unitRepository;

    @Autowired
    public UnitController(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UnitCreateReqBody reqBody, Errors errors){

        if(errors.hasErrors())
            return ResponseEntity.badRequest()
                                .body(BadReqCodes.getRespBody(BadReqCodes.WRONG_INPUT));

        Unit unit = new Unit();
        unit.setName(reqBody.getName());

        try {
            unitRepository.save(unit);
        }catch (DataIntegrityViolationException ex){
            if(ex.contains(ConstraintViolationException.class)){
                return ResponseEntity.badRequest()
                        .body(BadReqCodes.getRespBody(BadReqCodes.UUNC_VIOLATION));
            }
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        //todo cannot delete unit if there are ingredients that use given unit
        try{
            unitRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            //this exception is thrown when we try to delete an entity that doesn't exits
            //do nothing
        }

        return ResponseEntity.ok().build();
    }

    //Now no one can delete an entity that we are trying to update here.
    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody UnitUpdateReqBody reqBody){

        try {

            if(unitRepository.existsUnitByName(reqBody.getName()))
                throw new BadReqException(reqBody.getName(), BadReqCodes.UUNC_VIOLATION.getCode(),"Unit");

            Unit unit = unitRepository.findById(id).orElseThrow(() -> new BadReqException(id, BadReqCodes.NO_SUCH_UNIT.getCode(),"Unit"));

            if(reqBody.getName() != null) unit.setName(reqBody.getName());

            unitRepository.save(unit);

        }catch (BadReqException ex){
            return ResponseEntity.badRequest().body(ex.getRespBody());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        try {
            UnitRespBody respBody = unitRepository.findById(id)
                                                    .map(u -> new UnitRespBody(u.getId(), u.getName()))
                                                    .orElseThrow(IllegalArgumentException::new);

            return ResponseEntity.ok(respBody);

        }catch (IllegalArgumentException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pname/{partname}")
    public ResponseEntity<?> findAllByPartName(@PathVariable String partname){
        final String key = "%" + partname + "%";

        List<UnitRespBody> resp = unitRepository.findByNameLike(key).stream()
                                                 .map(unit -> new UnitRespBody(unit.getId(),unit.getName()))
                                                 .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){

        List<UnitRespBody> resp = StreamSupport.stream(unitRepository.findAll().spliterator(),false)
                .map(u -> new UnitRespBody(u.getId(),u.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

}
