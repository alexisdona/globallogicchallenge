package com.globallogic.challenge.controller.auth;

import com.globallogic.challenge.dto.AuthenticationResponse;
import com.globallogic.challenge.dto.RegisterResponse;
import com.globallogic.challenge.dto.UserDto;
import com.globallogic.challenge.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path="/register")
    public ResponseEntity<RegisterResponse> createUser(@RequestBody @NonNull UserDto userDto) {
        return ResponseEntity.ok(authenticationService.register(userDto));
    }

    @PostMapping(path="/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @NonNull AuthenticationRequest authentincationRequest) {
        return new ResponseEntity<>(authenticationService.login(authentincationRequest), HttpStatus.OK);
    }
}
