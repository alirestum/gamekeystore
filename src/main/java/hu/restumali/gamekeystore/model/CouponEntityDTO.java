package hu.restumali.gamekeystore.model;

import hu.restumali.gamekeystore.validators.FutureStringDateConstraint;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Optional;

@Data
public class CouponEntityDTO {

    private Long id;

    @Valid
    @Digits(integer = 10, fraction = 10, message = "Discount amount must be a number!")
    @Positive(message = "Discount can't be a negative number!")
    @NotNull(message = "Discount can't be empty!")
    private Integer discount;

    @Valid
    @NotEmpty(message = "Coupon must have a name!")
    private String name;

    @Valid
    @FutureStringDateConstraint(message = "Expiration date must be in the future!")
    private String validUntil;

    private Integer timesUsed;

    public CouponEntityDTO(){}

    public CouponEntityDTO(CouponEntity base, Integer timesUsed){
        this.id = base.getId();
        this.name = base.getName();
        this.discount = base.getDiscount();
        this.validUntil = base.getValidUntil().toString();
        this.timesUsed = timesUsed;
    }
}
