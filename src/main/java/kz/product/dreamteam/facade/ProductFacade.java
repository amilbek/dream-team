package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.ProductDTO;
import kz.product.dreamteam.model.dto.ProductSaveDTO;
import kz.product.dreamteam.model.dto.request.FilterRequest;
import kz.product.dreamteam.model.dto.request.SortRequest;
import kz.product.dreamteam.model.dto.request.SearchRequest;
import org.bson.types.ObjectId;

import java.util.Collection;

public interface ProductFacade {

    ProductDTO saveProduct(ProductSaveDTO productSaveDTO);

    ProductDTO getProduct(ObjectId id);

    ProductDTO editProduct(ObjectId id, ProductSaveDTO productSaveDTO);

    void deleteProduct(ObjectId id);

    ProductDTO likeProduct(ObjectId id);

    Collection<ProductDTO> search(SearchRequest<FilterRequest, SortRequest> request);

    Collection<ProductDTO> recommendedList();
}
