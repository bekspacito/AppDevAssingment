package edu.myrza.appdev.labone.service;

import edu.myrza.appdev.labone.domain.Ingredient;
import edu.myrza.appdev.labone.domain.Unit;
import edu.myrza.appdev.labone.error.BadReqCodes;
import edu.myrza.appdev.labone.error.BadReqResponseBody;
import edu.myrza.appdev.labone.exception.BadReqException;
import edu.myrza.appdev.labone.payload.ingredient.CreateReqBody;
import edu.myrza.appdev.labone.payload.ingredient.UpdateReqBody;
import edu.myrza.appdev.labone.repository.IngredientRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngredientService {

    private final IngredientRepository ingRepository;
    private final UnitService unitService;

    public IngredientService(IngredientRepository ingRepository,
                             UnitService unitService)
    {
        this.ingRepository = ingRepository;
        this.unitService = unitService;
    }

    public void save(CreateReqBody reqBody){
        Unit unit = unitService.findById(reqBody.getUnitId());

        Ingredient newIng = new Ingredient();
        newIng.setName(reqBody.getName());
        newIng.setPrice(reqBody.getPrice());
        newIng.setUnit(unit);

        try {

            ingRepository.save(newIng);

        }catch (DataIntegrityViolationException ex){
            if(ConstraintViolationException.class.isAssignableFrom(ex.getCause().getClass()))
                throw (ConstraintViolationException) ex.getCause();
        }
    }

    @Transactional
    public void update(Long id,UpdateReqBody reqBody){

        Ingredient ingToUpd = findById(id);

        reqBody.getName().ifPresent(ingToUpd::setName);
        reqBody.getPrice().ifPresent(ingToUpd::setBDPrice);
        if(reqBody.getUnitId() != null) {
            Unit unit = unitService.findById(reqBody.getUnitId());
            ingToUpd.setUnit(unit);
        }

    }

    public void delete(Long id){

        try{
            ingRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            //this exception is thrown when we try to delete an entity that doesn't exits
            //do nothing
        }catch (ConstraintViolationException cvex){
            //We try to delete an ingredient that is used by dish(es)
            if(cvex.getConstraintName().contains("FK_DISH_ING_ING")){
                BadReqResponseBody body = new BadReqResponseBody
                        .Builder(BadReqCodes.ING_IS_IN_USE)
                        .identifier(id)
                        .build();

                throw new BadReqException(body);
            }
        }

    }

    public Ingredient findById(Long id){
        return ingRepository.findById(id)
                .orElseThrow(() -> {
                    BadReqResponseBody body = new BadReqResponseBody.Builder(BadReqCodes.NO_SUCH_INGR)
                            .identifier(id)
                            .build();
                    return new BadReqException(body);
                });
    }

    public Iterable<Ingredient> findByPartName(String partname){
        String key = "%" + partname + "%";
        return ingRepository.findByNameLike(key);
    }

    public Iterable<Ingredient> findAll(){
        return ingRepository.findAll();
    }
}
