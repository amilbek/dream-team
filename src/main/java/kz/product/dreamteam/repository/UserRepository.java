package kz.product.dreamteam.repository;

import kz.product.dreamteam.model.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<UserDetails> findByEmail(String username);

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
