package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.Dish;
import org.springframework.data.repository.CrudRepository;

public interface DishRepository extends CrudRepository<Dish,Long>{

}