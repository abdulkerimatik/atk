package com.atk.mail.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.atk.mail.domain.repo.UserRepo;
import com.atk.mail.dto.AccountDTO;
import com.atk.mail.entity.User;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserRepo userRepo;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDto = (AccountDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (accountDto.getUsername().length() < 6 || accountDto.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (userRepo.findByUsername(accountDto.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (accountDto.getPassword().length() < 8 || accountDto.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!accountDto.getPassword().equals(accountDto.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
