package com.globallogic.challenge.service;

import com.globallogic.challenge.domain.entity.Phone;
import com.globallogic.challenge.domain.entity.Role;
import com.globallogic.challenge.domain.entity.User;
import com.globallogic.challenge.dto.UserDto;
import com.globallogic.challenge.exception.APIException;
import com.globallogic.challenge.repository.PhoneRepository;
import com.globallogic.challenge.repository.UserRepository;
import com.globallogic.challenge.security.RegexValidator;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d.*\\d)(?!.*[^a-zA-Z0-9])(?!.*[a-z]{13,})(?=.*[a-z]).{8,12}$";

    private final UserRepository userRepository;

    private final PhoneRepository phoneRepository;

    private final PasswordEncoder passwordEncoder;

    RegexValidator regexValidator;

    public UserService(UserRepository userRepository, PhoneRepository phoneRepository, PasswordEncoder passwordEncoder, RegexValidator regexValidator) {
       this.userRepository = userRepository;
       this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
        this.regexValidator = regexValidator;
    }

    public User createUser(UserDto userDto) {
        LocalDateTime now = LocalDateTime.now();

        validateEmail(userDto.getEmail());
        validatePassword(userDto.getPassword());
        existingUser(userDto.getEmail());

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .dateCreated(now)
                .lastLogin(now)
                .active(true)
                .role(Role.USER)
                .build();

        List<Phone> phones = buildPhones(userDto, user);
        phoneRepository.saveAll(phones);

        return userRepository.save(user);
    }

    private static List<Phone> buildPhones(UserDto userDto, User user) {
        if (userDto.getPhones() != null) {
            return userDto.getPhones().stream().map(
                    phoneDto -> Phone.builder()
                            .number(phoneDto.getNumber())
                            .cityCode(phoneDto.getCityCode())
                            .countryCode(phoneDto.getCountryCode())
                            .user(user)
                            .build()
            ).toList();
        }
        return new ArrayList<>();
    }

    public void updateLastLoginDate(String userId) {
        User user = userRepository.getReferenceById(userId);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "You must provide a password");
        }
        regexValidator.isValid(password, PASSWORD_PATTERN,
                "The password you provided does not match our policy. only 2 digits, only 1 uppercase letter, any lowercase letters. 8 to 12 long");
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "You must provide an email");
        }

        regexValidator.isValid(email, EMAIL_REGEX, "User's email is not a valid format. Should be user@someDomain.com.ar for example ");
    }

    public void existingUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
       if (user.isPresent()) {
           throw new APIException(HttpStatus.CONFLICT, "The user is already created");
       }
    }
}


