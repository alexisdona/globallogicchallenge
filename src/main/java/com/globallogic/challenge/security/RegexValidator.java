package com.globallogic.challenge.security;

import com.globallogic.challenge.exception.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexValidator {

    public void isValid(String sequence, final String regex, String errorMessage) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sequence);
        if (!matcher.matches()) {
            throw new APIException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }
}
