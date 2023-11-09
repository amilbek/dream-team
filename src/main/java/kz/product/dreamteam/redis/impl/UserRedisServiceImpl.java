package kz.product.dreamteam.redis.impl;

import jakarta.annotation.PostConstruct;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.redis.UserRedisService;
import kz.product.dreamteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserRedisServiceImpl implements UserRedisService {

    private static final String MY_ACCOUNT_KEY = "MY_ACCOUNT: ";

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService service;

    private HashOperations<String, ObjectId, User> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public User getUser() {
        ObjectId userId = service.getUser().getId();
        String key = MY_ACCOUNT_KEY + userId;
        return hashOperations.get(key, userId);
    }

    @Override
    public void addUserToCache(User user) {
        ObjectId userId = user.getId();
        String key = MY_ACCOUNT_KEY + userId;
        hashOperations.delete(key, userId, user);
        hashOperations.put(key, userId, user);
        redisTemplate.expireAt(MY_ACCOUNT_KEY + userId, Instant.now());
    }


    @Override
    public void deleteUserFromCache(User user) {
        ObjectId userId = user.getId();
        String key = MY_ACCOUNT_KEY + userId;
        hashOperations.delete(key, userId, user);
    }
}
