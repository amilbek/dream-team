package kz.product.dreamteam.service;

import kz.product.dreamteam.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUserName(String token);

    String generateToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);
}
