package hu.restumali.gamekeystore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer quantity;

    private Integer productSum;

    @ManyToOne(targetEntity = OrderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public OrderItemEntity(){}

    public OrderItemEntity(ProductEntity product){
        this.product = product;
        this.quantity = 1;
        this.productSum = product.getSalePrice() == null ? product.getBasePrice() : product.getSalePrice();
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
        this.productSum = quantity * (this.product.getSalePrice() == null ? this.product.getBasePrice() : this.product.getSalePrice());
    }

    public void updateQuantity(Integer quantity){
        this.quantity = quantity;
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
