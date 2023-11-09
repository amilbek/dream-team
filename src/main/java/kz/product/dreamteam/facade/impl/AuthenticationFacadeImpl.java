package kz.product.dreamteam.facade.impl;

import kz.product.dreamteam.exception.CustomException;
import kz.product.dreamteam.facade.AuthenticationFacade;
import kz.product.dreamteam.model.dto.response.JwtAuthenticationResponse;
import kz.product.dreamteam.model.dto.request.SignInRequest;
import kz.product.dreamteam.model.dto.request.SignUpRequest;
import kz.product.dreamteam.model.dto.UserDTO;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.model.entity.enums.Role;
import kz.product.dreamteam.redis.UserRedisService;
import kz.product.dreamteam.service.JwtService;
import kz.product.dreamteam.service.UserService;
import kz.product.dreamteam.utils.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static kz.product.dreamteam.constants.CommonErrors.*;
import static kz.product.dreamteam.utils.UserSignUpValidation.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserService service;
    private final UserRedisService redisService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Override
    public UserDTO signUpUser(SignUpRequest signUpRequest) {
        checkUserValidations(signUpRequest);
        User savedUser = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .birthDate(signUpRequest.getBirthDate())
                .email(signUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .isDeleted(false)
                .build();
        return ModelMapperUtil.map(service.save(savedUser), UserDTO.class);
    }

    @Override
    public JwtAuthenticationResponse signInUser(SignInRequest signInRequest) {
        User user = service.getUserByEmail(signInRequest.getEmail());
        redisService.addUserToCache(user);
        if (!bCryptPasswordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new CustomException(String.valueOf(INVALID_USER_CREDENTIALS));
        }
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    private void checkUserValidations(SignUpRequest signUpRequest) {
        List<String> errors = new ArrayList<>();
        if (service.existsUserByEmail(signUpRequest.getEmail())) {
            errors.add(String.valueOf(EMAIL_EXISTS));
        }
        if (!isValidBirthDate(signUpRequest.getBirthDate())) {
            errors.add(String.valueOf(AGE_LESS_THAN_18));
        }
        if (!isValidEmail(signUpRequest.getEmail())) {
            errors.add(String.valueOf(INVALID_EMAIL));
        }
        if (!isValidPassword(signUpRequest.getPassword())) {
            errors.add(String.valueOf(INVALID_PASSWORD));
        }
        if (!isValidPasswordConfirmation(signUpRequest)) {
            errors.add(String.valueOf(INVALID_PASSWORD_CONFIRMATION));
        }
        if (!errors.isEmpty()) {
            throw new CustomException(String.valueOf(errors));
        }
    }
}
