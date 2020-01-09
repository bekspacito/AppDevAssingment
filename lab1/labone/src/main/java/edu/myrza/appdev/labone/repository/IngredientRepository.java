package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient,Long>{

}