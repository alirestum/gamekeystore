package hu.restumali.gamekeystore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Email address is required!")
    @Email(message = "Not a valid email address!")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters")
    private String username;

    @NotBlank(message = "Password is required!")
    @Size(min = 5, message = "Password must be at least 5 characters long!")
    private String password;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Collection<OrderEntity> orders;


}
