package com.globallogic.challenge.service;

import com.globallogic.challenge.domain.entity.Phone;
import com.globallogic.challenge.domain.entity.Role;
import com.globallogic.challenge.domain.entity.User;
import com.globallogic.challenge.dto.UserDto;
import com.globallogic.challenge.exception.APIException;
import com.globallogic.challenge.repository.UserRepository;
import com.globallogic.challenge.security.RegexValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RegexValidator regexValidator;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPassword("Test1234");

    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword("encodedPassword");
        user.setDateCreated(null);
        user.setLastLogin(null);
        user.setActive(true);
        user.setRole(Role.USER);

        List<Phone> phones = Arrays.asList(Phone.builder().id(1L).number(131444).cityCode(11).countryCode("AR").build());

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.createUser(userDto);

        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.isActive(), result.isActive());
        assertEquals(user.getRole(), result.getRole());
        assertNull(result.getDateCreated());
        assertNull(result.getLastLogin());

        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        User existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail(userDto.getEmail());

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(existingUser));

        APIException exception = assertThrows(APIException.class, () -> userService.createUser(userDto));

        assertEquals("The user is already created", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testCreateUserWithInvalidEmail() {

        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test.com");
        userDto.setPassword("Test1234");


        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        doThrow(new APIException(HttpStatus.BAD_REQUEST, "email error")).when(regexValidator).isValid(anyString(), anyString(), anyString());
        APIException exception = assertThrows(APIException.class, () -> userService.createUser(userDto));

        assertEquals("email error", exception.getMessage());

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testCreateUserWithnullEmail() {
        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail(null);
        userDto.setPassword("Test1234");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        APIException exception = assertThrows(APIException.class, () -> userService.createUser(userDto));

        assertEquals("You must provide an email", exception.getMessage());

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }


}
