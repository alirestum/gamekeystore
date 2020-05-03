package hu.restumali.gamekeystore.model;

import hu.restumali.gamekeystore.validators.PasswordMatchConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@PasswordMatchConstraint
public class UserDTO {

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String password;

    private String passwordConfirm;

    @NotNull
    @Email
    private String email;
}
