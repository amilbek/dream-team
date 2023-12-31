package kz.product.dreamteam.service;

import kz.product.dreamteam.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    User save(User user);

    User getUser();

    boolean existsUserByEmail(String email);

    User getUserByEmail(String email);

    UserDetailsService userDetailsService();
}
