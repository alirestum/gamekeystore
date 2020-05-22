package hu.restumali.gamekeystore.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @NotBlank(message = "Email address is required!")
    @Email(message = "Not a valid email address!")
    @Getter @Setter
    private String email;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String password;

    @Embedded
    @Getter @Setter
    private Address address;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Getter @Setter
    private List<OrderEntity> orders;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private List<UserRoleType> role;


}
