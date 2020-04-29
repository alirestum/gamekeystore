package hu.restumali.gamekeystore.validators;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FutureStringDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureStringDateConstraint {
    String message() default "Invalid date!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
