package hu.restumali.gamekeystore.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Getter @Setter
    private UserEntity customer;

    @Getter @Setter
    private OrderStatusType status;

    @Getter @Setter
    private LocalDateTime orderDate;

    @Getter @Setter
    private Integer orderSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    @Getter @Setter
    private CouponEntity coupon;

    @OneToMany(targetEntity = OrderItemEntity.class, fetch = FetchType.LAZY, mappedBy = "order")
    @Getter @Setter
    private List<OrderItemEntity> items;

    @Embedded
    @Getter @Setter
    private Address billingAddress;
}
