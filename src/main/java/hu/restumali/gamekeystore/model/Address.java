package hu.restumali.gamekeystore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    @NotEmpty(message = "Country is required!")
    private String country;

    @NotEmpty(message = "State is required!")
    private String state;

    @NotEmpty(message = "Street is required!")
    private String street;

    @NotNull(message = "Number is required!")
    @Positive(message = "Number can't be negative!")
    private Integer number;

    private String extra;

    @Override
    public String toString() {
        return country + ", " + state + ", " + street + ", " + number;
    }
}
