package kz.product.dreamteam.repository;

import kz.product.dreamteam.model.entity.OrderPosition;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderPositionRepository extends MongoRepository<OrderPosition, ObjectId> {
}
