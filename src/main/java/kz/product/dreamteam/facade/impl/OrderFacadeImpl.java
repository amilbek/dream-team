package kz.product.dreamteam.facade.impl;

import kz.product.dreamteam.facade.OrderFacade;
import kz.product.dreamteam.model.dto.OrderDTO;
import kz.product.dreamteam.model.dto.OrderPositionSaveDTO;
import kz.product.dreamteam.model.dto.OrderSaveDTO;
import kz.product.dreamteam.model.dto.request.FilterRequest;
import kz.product.dreamteam.model.dto.request.SortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import kz.product.dreamteam.model.entity.Order;
import kz.product.dreamteam.model.entity.OrderPosition;
import kz.product.dreamteam.model.entity.Product;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.model.entity.enums.OrderStatus;
import kz.product.dreamteam.service.OrderService;
import kz.product.dreamteam.service.ProductService;
import kz.product.dreamteam.service.UserService;
import kz.product.dreamteam.utils.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService service;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public OrderDTO addToShoppingCart(OrderSaveDTO orderSaveDTO) {
        User user = userService.getUser();
        Order order = Order
                .builder()
                .orderPositions(orderSaveDTO.getOrderPositions().stream().map(this::toEntity).toList())
                .totalSum(BigDecimal.valueOf(orderSaveDTO.getOrderPositions().stream().map(this::toTotalSum).reduce(Double::sum).orElse(0.0)))
                .user(user)
                .build();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.IN_SHOPPING_CARD);
        return ModelMapperUtil.map(service.makeOrder(order), OrderDTO.class);
    }

    @Override
    public OrderDTO makeOrder(ObjectId id) {
        Order order = service.getOrderById(id);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDERED);
        return ModelMapperUtil.map(service.makeOrder(order), OrderDTO.class);
    }

    @Override
    public Collection<OrderDTO> search(SearchRequest<FilterRequest, SortRequest> request) {
        User user = userService.getUser();
        return service.search(request, user)
                .stream()
                .map(x -> ModelMapperUtil.map(x, OrderDTO.class)).toList();
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
        return product.getPrice().multiply(BigDecimal.valueOf(orderPositionSaveDTO.getCount())).doubleValue();
    }
}
