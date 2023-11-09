package kz.product.dreamteam.service.impl;

import kz.product.dreamteam.model.entity.Order;
import kz.product.dreamteam.repository.OrderRepository;
import kz.product.dreamteam.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    //TODO: saving order position
    @Override
    public Order makeOrder(Order order) {
        return repository.save(order);
    }
}
