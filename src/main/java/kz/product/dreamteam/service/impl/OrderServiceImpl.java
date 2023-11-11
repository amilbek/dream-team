package kz.product.dreamteam.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.product.dreamteam.model.entity.Order;
import kz.product.dreamteam.repository.OrderRepository;
import kz.product.dreamteam.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    //TODO: saving order position
    @Override
    public Order makeOrder(Order order) {
        return repository.save(order);
    }

    @Override
    public Order getOrderById(ObjectId id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @Override
    public List<Order> getOrdersByUser(ObjectId userId) {
        return repository.findAllByUserId(userId);
    }
}
