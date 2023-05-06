package com.globallogic.challenge.service;

import com.globallogic.challenge.controller.auth.AuthenticationRequest;
import com.globallogic.challenge.domain.entity.Phone;
import com.globallogic.challenge.domain.entity.User;
import com.globallogic.challenge.dto.AuthenticationResponse;
import com.globallogic.challenge.dto.RegisterResponse;
import com.globallogic.challenge.dto.UserDto;
import com.globallogic.challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        UserDto userDto = new UserDto();
        userDto.setName("alexis");
        userDto.setEmail("alexis.dona@mail.com");
        userDto.setPassword("password");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID().toString());
        savedUser.setName("Alexis");
        savedUser.setEmail("alexis.dona@gmail.com");
        savedUser.setPassword("password");

        String token = "jwt-token";

        when(userService.createUser(userDto)).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn(token);

        RegisterResponse registerResponse = authenticationService.register(userDto);

        assertEquals(savedUser.getId().toString(), registerResponse.getId());
        assertEquals(savedUser.getDateCreated(), registerResponse.getCreated());
        assertEquals(savedUser.getLastLogin(), registerResponse.getLastLogin());
        assertEquals(savedUser.isActive(), registerResponse.isActive());
        assertEquals(token, registerResponse.getToken());

        verify(userService).createUser(userDto);
        verify(jwtService).generateToken(savedUser);
    }

    @Test
    public void testLogin() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("alexis.dona@gmail.com");
        authenticationRequest.setPassword("password");

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Alexis");
        user.setEmail("alexis.dona@gmail.com");
        user.setPassword("password");

        Phone phone1 = new Phone();
        phone1.setNumber(123456789);
        phone1.setCityCode(1);
        phone1.setCountryCode("AR");

        Phone phone2 = new Phone();
        phone2.setNumber(987654321);
        phone2.setCityCode(2);
        phone2.setCountryCode("BR");

        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));

        String token = "jwt-token";

        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(token);

        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);

        assertEquals(user.getId(), authenticationResponse.getId());
        assertEquals(user.getDateCreated(), authenticationResponse.getCreated());
        assertEquals(user.isActive(), authenticationResponse.isActive());
        assertEquals(user.getName(), authenticationResponse.getName());
        assertEquals(user.getEmail(), authenticationResponse.getEmail());
        assertEquals(token, authenticationResponse.getToken());


        verify(userRepository).findByEmail(authenticationRequest.getEmail());
        verify(jwtService).generateToken(user);
        verify(userService).updateLastLoginDate(user.getId());
    }
}
