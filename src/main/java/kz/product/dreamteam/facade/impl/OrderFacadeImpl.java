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
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        Order shoppingCart = service.getOrderInShoppingCart(user.getId());
        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());
        if (shoppingCart == null) {
            // Case 1: If user has no shopping cart, create a new one
            shoppingCart = createNewShoppingCart(user, product);
        } else {
            // Case 2: Check if the product is already in the shopping cart
            Optional<OrderPosition> existingPosition = findOrderPositionByProduct(shoppingCart, product);

            if (existingPosition.isPresent()) {
                // Case 2: If product is already in the shopping cart, increment the count by one
                existingPosition.get().setCount(existingPosition.get().getCount() + 1);
            } else {
                // Case 3: If product is not in the shopping cart, add a new position
                OrderPosition newOrderPosition = OrderPosition.builder()
                        .product(product)
                        .count(1)
                        .build();
                shoppingCart.getOrderPositions().add(newOrderPosition);
            }
        }
        updateTotalSum(shoppingCart);
        return ModelMapperUtil.map(service.save(shoppingCart), OrderDTO.class);
    }

    @Override
    public OrderDTO removeFromShoppingCart(OrderPositionSaveDTO orderPositionSaveDTO) {
        User user = userService.getUser();
        Order shoppingCart = service.getOrderInShoppingCart(user.getId());
        Product product = productService.getProduct(orderPositionSaveDTO.getProductId());

        if (shoppingCart != null) {
            // Check if the product is in the shopping cart
            Optional<OrderPosition> existingPosition = findOrderPositionByProduct(shoppingCart, product);

            if (existingPosition.isPresent()) {
                // If the count is greater than 1, decrement by one
                if (existingPosition.get().getCount() > 1) {
                    existingPosition.get().setCount(existingPosition.get().getCount() - 1);
                } else {
                    // If it is the last product, remove it from the shopping cart
                    shoppingCart.getOrderPositions().remove(existingPosition.get());

                    // If there are no other products, delete the user's shopping cart
                    if (shoppingCart.getOrderPositions().isEmpty()) {
                        service.remove(shoppingCart);
                        return null; // Shopping cart is deleted, return null or handle as needed
                    }
                }

                // Update total sum and save the shopping cart
                updateTotalSum(shoppingCart);
                return ModelMapperUtil.map(service.save(shoppingCart), OrderDTO.class);
            }
        }

        // If the product is not in the shopping cart, return null or handle as needed
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

    private Order createNewShoppingCart(User user, Product product) {
        Order order = new Order();
        OrderPosition orderPosition = OrderPosition.builder()
                .product(product)
                .count(1)
                .build();
        List<OrderPosition> orderPositions = new ArrayList<>();
        orderPositions.add(orderPosition);
        order.setOrderPositions(orderPositions);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.IN_SHOPPING_CART);
        order.setTotalSum(product.getPrice());
        return order;
    }

    private Optional<OrderPosition> findOrderPositionByProduct(Order shoppingCart, Product product) {
        return shoppingCart.getOrderPositions().stream()
                .filter(position -> position.getProduct().equals(product))
                .findFirst();
    }

    private void updateTotalSum(Order shoppingCart) {
        BigDecimal totalSum = shoppingCart.getOrderPositions().stream()
                .map(position -> position.getProduct().getPrice().bigDecimalValue().multiply(BigDecimal.valueOf(position.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        shoppingCart.setTotalSum(Decimal128.parse(totalSum.toString()));
    }
}
