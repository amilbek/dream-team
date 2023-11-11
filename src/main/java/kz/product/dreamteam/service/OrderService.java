package kz.product.dreamteam.service;

import kz.product.dreamteam.model.entity.Order;
import org.bson.types.ObjectId;

public interface OrderService {

    Order makeOrder(Order order);

    Order getOrderById(ObjectId id);
}
