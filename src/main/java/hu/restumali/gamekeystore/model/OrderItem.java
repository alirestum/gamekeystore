package hu.restumali.gamekeystore.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class OrderItem {

    private ProductEntity product;

    private Integer quantity;

    private Integer productSum;

    public OrderItem(){}

    public OrderItem(ProductEntity product){
        this.product = product;
        this.quantity = 1;
        this.productSum = product.getSalePrice() == null ? product.getBasePrice() : product.getSalePrice();
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
        this.productSum = quantity * this.product.getSalePrice();
    }
}
