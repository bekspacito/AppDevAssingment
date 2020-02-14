package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish,Long>{

    List<Dish> findByNameLike(String part);

}