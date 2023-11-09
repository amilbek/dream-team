package kz.product.dreamteam.controller;

import kz.product.dreamteam.facade.AccountFacade;
import kz.product.dreamteam.model.dto.UserDTO;
import kz.product.dreamteam.model.dto.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kz.product.dreamteam.constants.CommonConstants.ACCOUNT_DELETED;

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

    @GetMapping("/delete-user")
    public ResponseEntity<?> deleteUserAccount() {
        accountFacade.deleteUserAccount();
        return ResponseEntity.ok(ACCOUNT_DELETED);
    }
}
