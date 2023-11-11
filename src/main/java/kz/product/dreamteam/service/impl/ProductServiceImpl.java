package kz.product.dreamteam.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.product.dreamteam.model.dto.request.ProductFilterRequest;
import kz.product.dreamteam.model.dto.request.ProductSortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import kz.product.dreamteam.model.entity.Product;
import kz.product.dreamteam.repository.ProductRepository;
import kz.product.dreamteam.service.ProductService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kz.product.dreamteam.utils.Util.notNullOrEmptyStr;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String COLLECTION_NAME = "products";
    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String CATEGORY_FIELD = "category";
    private static final String PRICE_FIELD = "price";

    private final ProductRepository repository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public Product getProduct(ObjectId id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public Page<Product> search(SearchRequest<ProductFilterRequest, ProductSortRequest> searchRequest) {
        ProductFilterRequest filter = searchRequest.getFilter();
        List<AggregationOperation> stages = new ArrayList<>();
        if (notNullOrEmptyStr(filter.getValue())) {
            Criteria regexCriteria = new Criteria().orOperator(
                    Criteria.where(NAME_FIELD).regex(filter.getValue(), "i"),
                    Criteria.where(DESCRIPTION_FIELD).regex(filter.getValue(), "i")
            );
            stages.add(Aggregation.match(regexCriteria));
        }
        if (notNullOrEmptyStr(filter.getCategory())) {
            stages.add(Aggregation.match(Criteria.where(CATEGORY_FIELD).is(filter.getCategory())));
        }

        if (nonNull(filter.getSumStart()) && nonNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(PRICE_FIELD).gte(filter.getSumStart()).lte(filter.getSumEnd())));
        }
        if (nonNull(filter.getSumStart()) && isNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(PRICE_FIELD).gte(filter.getSumStart())));
        }
        if (isNull(filter.getSumStart()) && nonNull(filter.getSumEnd())) {
            stages.add(Aggregation.match(Criteria.where(PRICE_FIELD).lte(filter.getSumEnd())));
        }

        Sort.Direction sortDirection = Sort.Direction.fromString(searchRequest.getSorting().getSortOrder());
        stages.add(Aggregation.sort(sortDirection, searchRequest.getSorting().getSortBy()));

        PageRequest pageRequest = PageRequest.of(searchRequest.getPageRequest().getPage(), searchRequest.getPageRequest().getLimit());
        stages.add(Aggregation.skip(pageRequest.getOffset()));
        stages.add(Aggregation.limit(pageRequest.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(stages);
        AggregationResults<Product> result = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Product.class);
        List<Product> products = result.getMappedResults();
        return new PageImpl<>(products, pageRequest, products.size());
    }


    @Override
    public List<Product> getProductsByLikedUser(ObjectId userId) {
        return repository.findByLikedUsersId(userId);
    }

    @Override
    public List<Product> getProductsByViewedUser(ObjectId userId) {
        return repository.findByViewedUsersId(userId);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public List<Product> getAllByCategoriesIn(List<String> categories) {
        return repository.findByCategoryIn(categories);
    }

    @Override
    public List<Product> getAllByPricesBetween(BigDecimal min, BigDecimal max) {
        return repository.findByPriceBetween(min, max);
    }
}
