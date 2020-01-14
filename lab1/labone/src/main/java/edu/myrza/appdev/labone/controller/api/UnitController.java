package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.payload.unit.UnitReqBody;
import edu.myrza.appdev.labone.payload.unit.UnitRespBody;
import edu.myrza.appdev.labone.repository.UnitRepository;
import edu.myrza.appdev.labone.util.BadReqSubcodes;
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
@RequestMapping("/api/unit")
public class UnitController {

    private UnitRepository unitRepository;

    @Autowired
    public UnitController(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UnitReqBody reqBody){

        Unit unit = new Unit();
        unit.setName(reqBody.getName());

        try {
            unitRepository.save(unit);
        }catch (DataIntegrityViolationException ex){
            if(ex.contains(ConstraintViolationException.class)){
                return ResponseEntity.badRequest()
                        .body(BadReqSubcodes.getRespBody(BadReqSubcodes.UUNC_VIOLATION));
            }
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        try{
            unitRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            //this exception is thrown when we try to delete an entity that doesn't exits
            //do nothing
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody UnitReqBody reqBody){

        Unit unit = new Unit();
        unit.setId(id);
        //todo unique name constraint
        unit.setName(reqBody.getName());

        //todo someone could delete an entity that we are trying to update here. Deal with it
        if(unitRepository.existsById(id)){
            unitRepository.save(unit);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
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

    @GetMapping("/pname")
    public ResponseEntity<?> findAllByPartName(@RequestBody UnitRespBody reqBody){
        final String key = "%" + reqBody.getName() + "%";

        List<UnitRespBody> resp = unitRepository.findByNameLike(key).stream()
                                                 .map(unit -> new UnitRespBody(unit.getId(),unit.getName()))
                                                 .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    @GetMapping("all")
    public ResponseEntity<?> findAll(){

        List<UnitRespBody> resp = StreamSupport.stream(unitRepository.findAll().spliterator(),false)
                .map(u -> new UnitRespBody(u.getId(),u.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

}
