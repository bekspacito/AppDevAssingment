package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long>{

}