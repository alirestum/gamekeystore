package hu.restumali.gamekeystore.model;

import javax.persistence.Embeddable;

@Embeddable
public class OrderItem {

    private ProductEntity product;

    private Integer quantity;

    private Integer productsum;
}
