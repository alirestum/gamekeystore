package hu.restumali.gamekeystore.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class OrderItem {

    private ProductEntity product;

    private Integer quantity;

    private Integer productsum;

    public void setQuantity(int quantity){
        this.quantity = quantity;
        this.productsum = quantity * this.product.getSalePrice();
    }
}
