package kz.product.dreamteam.service;

import kz.product.dreamteam.model.entity.Product;
import org.bson.types.ObjectId;

public interface ProductService {

    Product save(Product product);

    Product getProduct(ObjectId id);
}
