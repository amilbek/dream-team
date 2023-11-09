package kz.product.dreamteam.facade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.product.dreamteam.model.dto.UserDTO;
import kz.product.dreamteam.model.dto.UserUpdateDTO;

public interface AccountFacade {

    UserDTO getUserAccount();

    UserDTO updateUserAccount(UserUpdateDTO userUpdateDTO);

    void deleteUserAccount();

    void logOut(HttpServletRequest request, HttpServletResponse response);
}
