package hu.restumali.gamekeystore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "coupons")
public class CouponEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Digits(integer = 10, fraction = 10, message = "Discount amount must be a number!")
    @Positive(message = "Discount can't be a negative number!")
    private Integer discount;

    @NotEmpty(message = "Coupon must have a name!")
    private String name;


    private Long validUntil;

    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
    private List<OrderEntity> orders;
}
