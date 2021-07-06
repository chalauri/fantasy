package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserRequestValidator {

    public void validate(User user) {

        if (StringUtils.isBlank(user.getEmail())) {
            throw new ApiException("email should not be empty");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            throw new ApiException("password should not be empty");
        }

    }

}
