package edu.myrza.appdev.labone.repository;

import edu.myrza.appdev.labone.domain.order.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order,String> {

}
