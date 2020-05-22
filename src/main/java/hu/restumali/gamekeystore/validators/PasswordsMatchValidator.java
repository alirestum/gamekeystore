package hu.restumali.gamekeystore.validators;

import hu.restumali.gamekeystore.model.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordMatchConstraint, Object> {

    @Override
    public void initialize(PasswordMatchConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        UserDTO user = (UserDTO) value;
        if (user.getPasswordConfirm().isBlank()){
            return true;
        } else {
            return user.getPassword().equals(user.getPasswordConfirm());
        }
    }
}
