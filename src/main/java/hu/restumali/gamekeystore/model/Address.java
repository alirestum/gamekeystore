package hu.restumali.gamekeystore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    @NotBlank(message = "Country is required!")
    private String country;

    @NotBlank(message = "State is required!")
    private String state;

    @NotBlank(message = "Street is required!")
    private String street;

    @NotBlank(message = "Number is required!")
    private Integer number;

    private String extra;
}
