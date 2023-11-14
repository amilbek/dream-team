package kz.product.dreamteam.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.product.dreamteam.model.dto.request.FilterRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import kz.product.dreamteam.model.dto.request.SortRequest;
import kz.product.dreamteam.model.entity.Order;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.model.entity.enums.Role;
import kz.product.dreamteam.repository.OrderRepository;
import kz.product.dreamteam.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kz.product.dreamteam.model.entity.enums.OrderStatus.IN_SHOPPING_CART;
import static kz.product.dreamteam.utils.Util.notNullOrEmptyStr;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String COLLECTION_NAME = "orders";
    private static final String TOTAL_SUM_FIELD = "totalSum";
    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String ORDER_STATUS_FIELD = "orderStatus";

    private final OrderRepository repository;
    private final MongoTemplate mongoTemplate;

    //TODO: saving order position
    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    public void remove(Order order) {
        repository.delete(order);
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

    @Override
    public Page<Order> search(SearchRequest<FilterRequest, SortRequest> searchRequest, User user) {
        FilterRequest filter = searchRequest.getFilter();
        List<AggregationOperation> stages = new ArrayList<>();

        if (user.getRole().equals(Role.USER)) {
            stages.add(Aggregation.match(Criteria.where("user").is(user)));
        }

        if (notNullOrEmptyStr(filter.getValue())) {
            stages.add(Aggregation.lookup("orderPositions", "_id", "order._id", "positions"));
            stages.add(Aggregation.unwind("positions"));
            stages.add(Aggregation.lookup("products", "positions.product", "_id", "product"));
            stages.add(Aggregation.unwind("product"));
            Criteria regexCriteria = new Criteria().orOperator(
                    Criteria.where("product.name").regex(filter.getValue(), "i"),
                    Criteria.where("product.description").regex(filter.getValue(), "i")
            );
            stages.add(Aggregation.match(regexCriteria));
        }

        if (notNullOrEmptyStr(filter.getCategory())) {
            stages.add(Aggregation.lookup("orderPositions", "_id", "order._id", "positions"));
            stages.add(Aggregation.unwind("positions"));
            stages.add(Aggregation.lookup("products", "positions.product", "_id", "product"));
            stages.add(Aggregation.unwind("product"));
            stages.add(Aggregation.match(Criteria.where("product.category").is(filter.getCategory())));
        }

        if (notNullOrEmptyStr(filter.getOrderStatus())) {
            stages.add(Aggregation.match(Criteria.where(ORDER_STATUS_FIELD).is(filter.getOrderStatus())));
        }

        if (nonNull(filter.getSumStart()) && nonNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(TOTAL_SUM_FIELD).gte(filter.getSumStart()).lte(filter.getSumEnd())));
        }
        if (nonNull(filter.getSumStart()) && isNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(TOTAL_SUM_FIELD).gte(filter.getSumStart())));
        }
        if (isNull(filter.getSumStart()) && nonNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(TOTAL_SUM_FIELD).lte(filter.getSumEnd())));
        }

        if (nonNull(filter.getCreatedAtStart()) && nonNull(filter.getCreatedAtEnd())) {
            stages.add(Aggregation.match(Criteria.where(CREATED_AT_FIELD).gte(filter.getCreatedAtStart()).lte(filter.getCreatedAtEnd())));
        }
        if (nonNull(filter.getCreatedAtStart()) && isNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(CREATED_AT_FIELD).gte(filter.getCreatedAtStart())));
        }
        if (isNull(filter.getCreatedAtStart()) && nonNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(CREATED_AT_FIELD).lte(filter.getCreatedAtEnd())));
        }

        Sort.Direction sortDirection = Sort.Direction.fromString(searchRequest.getSorting().getSortOrder());
        stages.add(Aggregation.sort(sortDirection, searchRequest.getSorting().getSortBy()));

        PageRequest pageRequest = PageRequest.of(searchRequest.getPageRequest().getPage(), searchRequest.getPageRequest().getLimit());
        stages.add(Aggregation.skip(pageRequest.getOffset()));
        stages.add(Aggregation.limit(pageRequest.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(stages);
        AggregationResults<Order> result = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Order.class);
        List<Order> orders = result.getMappedResults();
        return new PageImpl<>(orders, pageRequest, orders.size());
    }

    @Override
    public Order getOrderInShoppingCart(ObjectId userId) {
        return repository.findByUserIdAndOrderStatus(userId, IN_SHOPPING_CART)
                .orElse(null);
    }
}
