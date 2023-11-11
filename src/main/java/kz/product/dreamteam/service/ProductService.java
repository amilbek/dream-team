package kz.product.dreamteam.service;

import kz.product.dreamteam.model.dto.request.ProductFilterRequest;
import kz.product.dreamteam.model.dto.request.ProductSortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import kz.product.dreamteam.model.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product save(Product product);

    Product getProduct(ObjectId id);

    @Transactional(readOnly = true)
    Page<Product> search(SearchRequest<ProductFilterRequest, ProductSortRequest> searchRequest);

    List<Product> getProductsByLikedUser(ObjectId userId);

    List<Product> getProductsByViewedUser(ObjectId userId);

    List<Product> getAllProducts();

    List<Product> getAllByCategoriesIn(List<String> categories);

    List<Product> getAllByPricesBetween(BigDecimal min, BigDecimal max);
}
