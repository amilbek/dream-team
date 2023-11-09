package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.UserDTO;
import kz.product.dreamteam.model.dto.UserUpdateDTO;

public interface AccountFacade {

    UserDTO getUserAccount();

    UserDTO updateUserAccount(UserUpdateDTO userUpdateDTO);

    void deleteUserAccount();
}
