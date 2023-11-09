package kz.product.dreamteam.facade;

import kz.product.dreamteam.model.dto.response.JwtAuthenticationResponse;
import kz.product.dreamteam.model.dto.request.SignInRequest;
import kz.product.dreamteam.model.dto.request.SignUpRequest;
import kz.product.dreamteam.model.dto.UserDTO;

public interface AuthenticationFacade {

    UserDTO signUpUser(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signInUser(SignInRequest signInRequest);
}
