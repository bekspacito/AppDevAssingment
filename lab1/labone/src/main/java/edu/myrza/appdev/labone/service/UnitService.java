package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.payload.unit.CreateReqBody;
import edu.myrza.appdev.labone.payload.unit.UpdateReqBody;
import edu.myrza.appdev.labone.repository.UnitRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    @Autowired
    public UnitService(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }

    public void create(CreateReqBody reqBody){
        Unit unit = new Unit();
        unit.setName(reqBody.getName());

        try {

            unitRepository.save(unit);

        }catch (DataIntegrityViolationException ex){
            if(ConstraintViolationException.class.isAssignableFrom(ex.getCause().getClass()))
                throw (ConstraintViolationException) ex.getCause();
        }

    }

    @Transactional
    public void update(Long id, UpdateReqBody reqBody){

        Unit unit = unitRepository.findById(id).orElseThrow(() -> {
            BadReqResponseBody resp = new BadReqResponseBody.Builder(BadReqCodes.NO_SUCH_UNIT)
                    .identifier(id)
                    .build();

            return new BadReqException(resp);
        });

        unit.setName(reqBody.getName());
    }

    @Transactional
    public void delete(Long id){
        try{
            unitRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            //this exception is thrown when we try to delete an entity that doesn't exits
            //do nothing
        }catch (DataIntegrityViolationException ex){
            if(ConstraintViolationException.class.isAssignableFrom(ex.getCause().getClass())){
                throw (ConstraintViolationException) ex.getCause();
            }
        }

    }

    public Unit findById(Long id){
        return unitRepository.findById(id)
                            .orElseThrow(() -> {
                                BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.NO_SUCH_UNIT)
                                        .identifier(id)
                                        .build();
                                return new BadReqException(body);
                            });
    }

    public List<Unit> findAllByPartName(String partname){
        final String key = "%" + partname + "%";

        return unitRepository.findByNameLike(key);
    }

    public Iterable<Unit> findAll(){
        return unitRepository.findAll();
    }
}
