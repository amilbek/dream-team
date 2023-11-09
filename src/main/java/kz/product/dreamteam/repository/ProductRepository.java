package kz.product.dreamteam.repository;

import kz.product.dreamteam.model.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {

    List<Product> findAllByIdIn(Collection<ObjectId> id);
}
