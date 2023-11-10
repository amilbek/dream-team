package kz.product.dreamteam.facade.impl;

import kz.product.dreamteam.facade.OrderFacade;
import kz.product.dreamteam.model.dto.OrderDTO;
import kz.product.dreamteam.model.dto.OrderPositionSaveDTO;
import kz.product.dreamteam.model.dto.OrderSaveDTO;
import kz.product.dreamteam.model.entity.Order;
import kz.product.dreamteam.model.entity.OrderPosition;
import kz.product.dreamteam.model.entity.Product;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.service.OrderService;
import kz.product.dreamteam.service.ProductService;
import kz.product.dreamteam.service.UserService;
import kz.product.dreamteam.utils.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService service;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public OrderDTO makeOrder(OrderSaveDTO orderSaveDTO) {
        User user = userService.getUser();
        Order order = Order
                .builder()
                .orderPositions(orderSaveDTO.getOrderPositions().stream().map(this::toEntity).collect(Collectors.toList()))
                .totalSum(BigDecimal.valueOf(orderSaveDTO.getOrderPositions().stream().map(this::toTotalSum).reduce(Double::sum).orElse(0.0)))
                .user(user)
                .build();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        return ModelMapperUtil.map(service.makeOrder(order), OrderDTO.class);
    }

    private OrderPosition toEntity(OrderPositionSaveDTO orderPositionSaveDTO) {
        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());
        return OrderPosition
                .builder()
                .product(product)
                .count(orderPositionSaveDTO.getCount())
                .build();
    }

    private double toTotalSum(OrderPositionSaveDTO orderPositionSaveDTO) {
        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());
        return product.getPrice().doubleValue();
    }
}
