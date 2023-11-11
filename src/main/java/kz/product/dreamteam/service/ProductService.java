package kz.product.dreamteam.service;

import kz.product.dreamteam.model.dto.request.ProductFilterRequest;
import kz.product.dreamteam.model.dto.request.ProductSortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import kz.product.dreamteam.model.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

    Product save(Product product);

    Product getProduct(ObjectId id);

    @Transactional(readOnly = true)
    Page<Product> search(SearchRequest<ProductFilterRequest, ProductSortRequest> searchRequest);
}
