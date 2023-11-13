package kz.product.dreamteam.facade.impl;

import kz.product.dreamteam.facade.OrderFacade;
import kz.product.dreamteam.model.dto.OrderDTO;
import kz.product.dreamteam.model.dto.OrderPositionSaveDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService service;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public OrderDTO addToShoppingCart(OrderPositionSaveDTO orderPositionSaveDTO) {
        User user = userService.getUser();
        Order order = service.getOrderInShoppingCart(user.getId());
        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());
        if (isNull(order)) {
            //creating new shopping cart
            order = new Order();
            OrderPosition orderPosition = OrderPosition
                    .builder()
                    .product(product)
                    .count(1)
                    .build();
            List<OrderPosition> orderPositions = new ArrayList<>();
            orderPositions.add(orderPosition);
            order.setOrderPositions(orderPositions);
            order.setUser(user);
            order.setOrderStatus(OrderStatus.IN_SHOPPING_CART);
            order.setTotalSum(product.getPrice());
            return ModelMapperUtil.map(service.save(order), OrderDTO.class);
        }
        //incrementing existing product
        for (OrderPosition orderPosition : order.getOrderPositions()) {
            log.info("Order position product: {}", orderPosition.getProduct());
            log.info("Product: {}", product);
            if (orderPosition.getProduct().equals(product)) {
                int count = orderPosition.getCount() + 1;
                orderPosition.setCount(count);
                order.setTotalSum(product.getPrice().multiply(BigDecimal.valueOf(count)));
                return ModelMapperUtil.map(service.save(order), OrderDTO.class);
            }
        }
        //adding new product
        OrderPosition orderPosition = OrderPosition
                .builder()
                .product(product)
                .count(1)
                .build();
        List<OrderPosition> orderPositions = order.getOrderPositions();
        orderPositions.add(orderPosition);
        order.setOrderPositions(orderPositions);
        BigDecimal totalSum = BigDecimal.valueOf(0.0);
        for (OrderPosition orderPosition1 : orderPositions) {
            totalSum = totalSum.add(orderPosition1.getProduct().getPrice().multiply(BigDecimal.valueOf(orderPosition1.getCount())));
        }
        order.setTotalSum(totalSum);
        return ModelMapperUtil.map(service.save(order), OrderDTO.class);
    }

    @Override
    public OrderDTO removeFromShoppingCart(OrderPositionSaveDTO orderPositionSaveDTO) {
        User user = userService.getUser();
        Order order = service.getOrderInShoppingCart(user.getId());
        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());

        //decrementing existing product
        for (OrderPosition orderPosition : order.getOrderPositions()) {
            log.info("Order position product: {}", orderPosition.getProduct());
            log.info("Product: {}", product);
            if (orderPosition.getProduct().equals(product)) {
                int count = orderPosition.getCount() - 1;
                orderPosition.setCount(count);
                order.setTotalSum(product.getPrice().multiply(BigDecimal.valueOf(count)));
                return ModelMapperUtil.map(service.save(order), OrderDTO.class);
            }
        }
        return null;
    }

    @Override
    public OrderDTO makeOrder(ObjectId id) {
        Order order = service.getOrderById(id);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDERED);
        return ModelMapperUtil.map(service.save(order), OrderDTO.class);
    }

    @Override
    public Collection<OrderDTO> search(SearchRequest<FilterRequest, SortRequest> request) {
        User user = userService.getUser();
        return service.search(request, user)
                .stream()
                .map(x -> ModelMapperUtil.map(x, OrderDTO.class)).toList();
    }

    @Override
    public OrderDTO getMyShoppingCart() {
        User user = userService.getUser();
        return ModelMapperUtil.map(service.getOrderInShoppingCart(user.getId()), OrderDTO.class);
    }

//    private OrderPosition toEntity(OrderPositionSaveDTO orderPositionSaveDTO) {
//        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());
//        return OrderPosition
//                .builder()
//                .product(product)
//                .count(orderPositionSaveDTO.getCount())
//                .build();
//    }

//    private double toTotalSum(OrderPositionSaveDTO orderPositionSaveDTO) {
//        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());
//        return product.getPrice().multiply(BigDecimal.valueOf(orderPositionSaveDTO.getCount())).doubleValue();
//    }
}
