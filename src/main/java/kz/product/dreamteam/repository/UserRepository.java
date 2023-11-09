package kz.product.dreamteam.repository;

import kz.product.dreamteam.model.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {

    Optional<UserDetails> findByEmail(String username);
}
