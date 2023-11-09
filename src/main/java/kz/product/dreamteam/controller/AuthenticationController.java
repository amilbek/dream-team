package kz.product.dreamteam.controller;

import kz.product.dreamteam.facade.AuthenticationFacade;
import kz.product.dreamteam.model.dto.response.JwtAuthenticationResponse;
import kz.product.dreamteam.model.dto.request.SignInRequest;
import kz.product.dreamteam.model.dto.request.SignUpRequest;
import kz.product.dreamteam.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationFacade facade;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> signup(@Validated @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(facade.signUpUser(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@Validated @RequestBody SignInRequest request) {
        return ResponseEntity.ok(facade.signInUser(request));
    }
}
