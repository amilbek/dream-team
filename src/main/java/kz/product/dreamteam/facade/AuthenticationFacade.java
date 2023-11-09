package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.JwtAuthenticationResponse;
import kz.product.dreamteam.model.dto.SignInRequest;
import kz.product.dreamteam.model.dto.SignUpRequest;
import kz.product.dreamteam.model.dto.UserDTO;

public interface AuthenticationFacade {

    UserDTO signUpUser(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signInUser(SignInRequest signInRequest);
}
