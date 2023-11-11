package kz.product.dreamteam.service;

import kz.product.dreamteam.model.entity.Order;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderService {

    Order makeOrder(Order order);

    Order getOrderById(ObjectId id);

    List<Order> getOrdersByUser(ObjectId userId);
}
