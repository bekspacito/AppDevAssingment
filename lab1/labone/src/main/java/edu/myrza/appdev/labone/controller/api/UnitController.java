package edu.myrza.appdev.labone.controller.api;

import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.payload.BadRequestRespBody;
import edu.myrza.appdev.labone.payload.UnitReqBody;
import edu.myrza.appdev.labone.payload.UnitRespBody;
import edu.myrza.appdev.labone.repository.UnitRepository;
import edu.myrza.appdev.labone.util.BadReqSubcodes;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @PostMapping
    public ResponseEntity<?> addUnit(@RequestBody UnitReqBody reqBody){

        Unit unit = new Unit();
        unit.setName(reqBody.getName());

        try {
            unitRepository.save(unit);
        }catch (DataIntegrityViolationException ex){
            if(ex.contains(ConstraintViolationException.class)){
                return ResponseEntity.badRequest()
                                      .body(getRespBody(BadReqSubcodes.UUNC_VIOLATION));
            }
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id,@RequestBody UnitReqBody reqBody){

        Unit unit = new Unit();
        unit.setId(id);
        unit.setName(reqBody.getName());

        if(unitRepository.existsById(id)){
            unitRepository.save(unit);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id){
        if(unitRepository.existsById(id))
                unitRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private static BadRequestRespBody getRespBody(BadReqSubcodes subcode){
        BadRequestRespBody respBody = new BadRequestRespBody();
        respBody.setCode(subcode.getCode());

        return respBody;
    }

}
