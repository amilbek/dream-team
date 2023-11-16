package kz.product.dreamteam.service;

import kz.product.dreamteam.model.dto.request.FilterRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import kz.product.dreamteam.model.dto.request.SortRequest;
import kz.product.dreamteam.model.entity.Order;
import kz.product.dreamteam.model.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    Order save(Order order);

    void remove(Order order);

    Order getOrderById(ObjectId id);

    List<Order> getOrdersByUser(ObjectId userId);

    @Transactional(readOnly = true)
    Page<Order> search(SearchRequest<FilterRequest, SortRequest> searchRequest, User user);

    Order getOrderInShoppingCart(ObjectId userId);

    Order getMyOrders(ObjectId userId);
}
