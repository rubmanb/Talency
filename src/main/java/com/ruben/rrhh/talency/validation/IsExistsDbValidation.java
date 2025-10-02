package com.ruben.rrhh.talency.validation;

import com.ruben.rrhh.talency.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IsExistsDbValidation implements ConstraintValidator<IsExistsDb, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!userService.existsByEmail(value)){
            return !userService.existsByUsername(value);
        }
        return !userService.existsByEmail(value);
    }

}
