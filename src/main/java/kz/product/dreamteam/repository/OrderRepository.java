package kz.product.dreamteam.repository;

import kz.product.dreamteam.model.entity.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    List<Order> findAllByUserId(ObjectId userId);
}
