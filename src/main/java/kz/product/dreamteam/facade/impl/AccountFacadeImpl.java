package kz.product.dreamteam.facade.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.product.dreamteam.exception.CustomException;
import kz.product.dreamteam.facade.AccountFacade;
import kz.product.dreamteam.model.dto.UserDTO;
import kz.product.dreamteam.model.dto.UserUpdateDTO;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.service.UserService;
import kz.product.dreamteam.utils.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static kz.product.dreamteam.constants.CommonErrors.*;
import static kz.product.dreamteam.utils.UserSignUpValidation.*;

@Service
@RequiredArgsConstructor
public class AccountFacadeImpl implements AccountFacade {

    private final UserService service;

    @Override
    public UserDTO getUserAccount() {
        return ModelMapperUtil.map(service.getUser(), UserDTO.class);
    }

    @Override
    public UserDTO updateUserAccount(UserUpdateDTO userUpdateDTO) {
        User user = service.getUser();
        checkUserUpdateValidations(user, userUpdateDTO);
        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setBirthDate(userUpdateDTO.getBirthDate());
        return ModelMapperUtil.map(service.save(user), UserDTO.class);
    }

    @Override
    public void deleteUserAccount() {
        User user = service.getUser();
        user.setIsDeleted(true);
        service.save(user);
    }

    @Override
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = service.getUser();
        SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler();
        if (user != null) {
            ctxLogOut.logout(request, response, auth);
        }
    }

    private void checkUserUpdateValidations(User user, UserUpdateDTO userUpdateDTO) {
        List<String> errors = new ArrayList<>();
        if (user.getEmail().equals(userUpdateDTO.getEmail()) && service.existsUserByEmail(userUpdateDTO.getEmail())) {
            errors.add(String.valueOf(EMAIL_EXISTS));
        }
        if (!isValidBirthDate(userUpdateDTO.getBirthDate())) {
            errors.add(String.valueOf(AGE_LESS_THAN_18));
        }
        if (!isValidEmail(userUpdateDTO.getEmail())) {
            errors.add(String.valueOf(INVALID_EMAIL));
        }
        if (!errors.isEmpty()) {
            throw new CustomException(String.valueOf(errors));
        }
    }
}
