package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient,Long>{

    List<Ingredient> findByNameLike(String part);

    boolean existsIngredientByName(String name);

}