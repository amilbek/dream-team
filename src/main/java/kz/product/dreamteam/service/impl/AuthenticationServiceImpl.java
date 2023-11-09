package kz.product.dreamteam.service.impl;

import kz.product.dreamteam.model.dto.JwtAuthenticationResponse;
import kz.product.dreamteam.model.dto.SignInRequest;
import kz.product.dreamteam.model.dto.SignUpRequest;
import kz.product.dreamteam.model.entity.User;
import kz.product.dreamteam.model.entity.enums.Role;
import kz.product.dreamteam.repository.UserRepository;
import kz.product.dreamteam.service.AuthenticationService;
import kz.product.dreamteam.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static kz.product.dreamteam.constants.CommonErrors.INVALID_USER_CREDENTIALS;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(INVALID_USER_CREDENTIALS)));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
