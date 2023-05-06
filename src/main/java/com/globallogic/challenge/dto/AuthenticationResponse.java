package com.globallogic.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("created")
    private LocalDateTime created;
    @JsonProperty("lastLogin")
    private LocalDateTime lastLogin;
    @JsonProperty("token")
    private String token;
    @JsonProperty("is_active")
    private boolean isActive;
    private String name;
    private String email;
    private Set<PhoneDto> phones;
}

