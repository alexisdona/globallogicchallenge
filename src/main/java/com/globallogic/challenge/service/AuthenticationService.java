package com.globallogic.challenge.service;

import com.globallogic.challenge.controller.auth.AuthenticationRequest;
import com.globallogic.challenge.domain.entity.User;
import com.globallogic.challenge.dto.AuthenticationResponse;
import com.globallogic.challenge.dto.PhoneDto;
import com.globallogic.challenge.dto.RegisterResponse;
import com.globallogic.challenge.dto.UserDto;
import com.globallogic.challenge.exception.APIException;
import com.globallogic.challenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(UserDto request) {
        LOGGER.info("Register request: {}", request);
        User savedUser = userService.createUser(request);

        var jwtToken = jwtService.generateToken(savedUser);

        return RegisterResponse.builder()
                .id(savedUser.getId().toString())
                .created(savedUser.getDateCreated())
                .lastLogin(savedUser.getLastLogin())
                .isActive(savedUser.isActive())
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found for login"));
        var jwtToken = jwtService.generateToken(user);

        userService.updateLastLoginDate(user.getId());

        return AuthenticationResponse.builder()
                .id(user.getId())
                .created(user.getDateCreated())
                .lastLogin(LocalDateTime.now())
                .token(jwtToken)
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .phones(buildPhones(user))
                .build();
    }

    private static Set<PhoneDto> buildPhones(User user) {
        return user.getPhones() != null ? user.getPhones().stream().map(phone -> new PhoneDto(
                phone.getNumber(), phone.getCityCode(), phone.getCountryCode())).collect(Collectors.toSet()) : new HashSet<>();
    }


}
