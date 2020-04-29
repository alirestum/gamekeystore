package hu.restumali.gamekeystore.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FutureStringDateValidator implements ConstraintValidator<FutureStringDateConstraint, String> {

    @Override
    public void initialize(FutureStringDateConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        long dateValue = 0;
        try {
            dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(value).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateValue > new Date().getTime();
    }
}
