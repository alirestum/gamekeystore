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
        this.productSum = quantity * (this.product.getSalePrice() == null ? this.product.getBasePrice() : this.product.getSalePrice());
    }

    public void increaseQuantity(){
        this.quantity++;
        this.productSum = this.quantity * (this.product.getSalePrice() == null ? this.product.getBasePrice() : this.product.getSalePrice());
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
