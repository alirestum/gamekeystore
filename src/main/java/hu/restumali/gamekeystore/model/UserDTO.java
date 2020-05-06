package hu.restumali.gamekeystore.model;

import hu.restumali.gamekeystore.validators.PasswordMatchConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@PasswordMatchConstraint(message = "Passwords does not match!")
public class UserDTO {

    @NotNull
    @NotEmpty(message = "This field is required!")
    private String firstName;

    @NotNull
    @NotEmpty(message = "This field is required!")
    private String lastName;

    @NotNull
    @NotEmpty(message = "This field is required!")
    private String password;

    private String passwordConfirm;

    @NotNull(message = "This field is required!")
    @Email(message = "Please add a valid email!")
    private String email;
}
