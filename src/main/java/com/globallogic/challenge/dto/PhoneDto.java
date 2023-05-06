package com.globallogic.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.globallogic.challenge.domain.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {
    private long number;
    @JsonProperty(value = "city_code")
    private int cityCode;
    @JsonProperty(value = "country_code")
    private String countryCode;
}
