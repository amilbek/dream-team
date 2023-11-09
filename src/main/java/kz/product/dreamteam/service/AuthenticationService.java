package kz.product.dreamteam.service;

import kz.product.dreamteam.model.dto.JwtAuthenticationResponse;
import kz.product.dreamteam.model.dto.SignInRequest;
import kz.product.dreamteam.model.dto.SignUpRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}
