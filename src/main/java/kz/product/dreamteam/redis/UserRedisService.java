package kz.product.dreamteam.redis;

import kz.product.dreamteam.model.entity.User;

public interface UserRedisService {

    User getUser();

    void addUserToCache(User user);

    void deleteUserFromCache(User user);
}
