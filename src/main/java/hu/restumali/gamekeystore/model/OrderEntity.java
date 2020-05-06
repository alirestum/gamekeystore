package hu.restumali.gamekeystore.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity customer;

    private OrderStatusType status;

    private LocalDateTime orderDate;

    private Integer orderSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    @ElementCollection
    @CollectionTable(name = "order_items")
    @OrderBy("product.name")
    private List<OrderItem> items;

    @Embedded
    private Address billingAddress;
}
