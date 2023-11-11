package kz.product.dreamteam.repository;

import kz.product.dreamteam.model.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {

    List<Product> findByLikedUsersId(ObjectId userId);

    List<Product> findByViewedUsersId(ObjectId userId);

    List<Product> findByCategoryIn(List<String> categories);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
}
