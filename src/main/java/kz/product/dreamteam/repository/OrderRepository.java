package kz.product.dreamteam.repository;

import kz.product.dreamteam.model.entity.Order;
import kz.product.dreamteam.model.entity.enums.OrderStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    List<Order> findAllByUserId(ObjectId userId);

    Optional<Order> findByUserIdAndOrderStatus(ObjectId userId, OrderStatus orderStatus);
}
