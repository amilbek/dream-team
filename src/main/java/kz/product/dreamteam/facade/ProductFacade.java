package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.ProductDTO;
import kz.product.dreamteam.model.dto.ProductSaveDTO;
import org.bson.types.ObjectId;

public interface ProductFacade {

    ProductDTO saveProduct(ProductSaveDTO productSaveDTO);

    ProductDTO getProduct(ObjectId id);

    ProductDTO editProduct(ObjectId id, ProductSaveDTO productSaveDTO);

    void deleteProduct(ObjectId id);

    ProductDTO likeProduct(ObjectId id);
}
