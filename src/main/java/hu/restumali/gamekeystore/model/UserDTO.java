package hu.restumali.gamekeystore.model;

import hu.restumali.gamekeystore.validators.PasswordMatchConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@PasswordMatchConstraint(message = "Passwords does not match!")
public class UserDTO {

    @NotNull
    @NotEmpty(message = "First name is required!")
    private String firstName;

    @NotNull
    @NotEmpty(message = "Last name is required!")
    private String lastName;

    @NotNull
    @NotEmpty(message = "Password is required!")
    @Size(min = 5, message = "Password must be at least 5 characters long!")
    private String password;


    private String passwordConfirm;

    @NotEmpty(message = "Email is required!")
    @Email(message = "Please add a valid email!")
    private String email;

    public UserDTO(UserEntity user){
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.passwordConfirm = user.getPassword();
    }
}
