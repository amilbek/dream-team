package kz.product.dreamteam.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.product.dreamteam.facade.AccountFacade;
import kz.product.dreamteam.model.dto.UserDTO;
import kz.product.dreamteam.model.dto.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kz.product.dreamteam.constants.CommonConstants.ACCOUNT_DELETED;
import static kz.product.dreamteam.constants.CommonConstants.LOG_OUT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountFacade accountFacade;

    @GetMapping("")
    public ResponseEntity<UserDTO> getUserAccount() {
        return ResponseEntity.ok(accountFacade.getUserAccount());
    }

    @PutMapping("/edit-user")
    public ResponseEntity<UserDTO> updateUserAccount(@Validated @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(accountFacade.updateUserAccount(userUpdateDTO));
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Object> deleteUserAccount() {
        accountFacade.deleteUserAccount();
        return ResponseEntity.ok(ACCOUNT_DELETED);
    }

    @GetMapping("/sign-out")
    public ResponseEntity<Object> signOut(HttpServletRequest request, HttpServletResponse response) {
        accountFacade.logOut(request, response);
        return ResponseEntity.ok(LOG_OUT);
    }
}
