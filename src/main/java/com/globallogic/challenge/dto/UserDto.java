package com.globallogic.challenge.dto;

import com.globallogic.challenge.domain.entity.Phone;
import com.globallogic.challenge.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime lastLogin;
    private LocalDateTime dateCreated;
    private boolean active;
    private List<PhoneDto> phones;

    public UserDto(User user) {
        this.id = user.getId().toString();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.dateCreated = user.getDateCreated();
        this.lastLogin = user.getLastLogin();
        Optional<List<Phone>> optionalPhones = Optional.ofNullable(user.getPhones());
        Stream<Phone> phoneStream = optionalPhones.stream().flatMap(Collection::stream);
        this.phones = phoneStream.map(Phone::transformToDto).collect(Collectors.toList());
    }

}
