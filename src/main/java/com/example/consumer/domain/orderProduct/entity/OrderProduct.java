package com.example.consumer.domain.orderProduct.entity;

import com.example.consumer.domain.basketProdcut.entity.BasketProduct;
import com.example.consumer.domain.order.entity.Order;
import com.example.consumer.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Id
    @Column(name = "order_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "count", nullable = false)
    private Long count;  // 주문 수량

    private Long price;

    @Enumerated(value = EnumType.STRING)
    private StatusEnum status = StatusEnum.PENDING;


    public OrderProduct(BasketProduct basketProduct) {
        this.product = basketProduct.getProduct();
        this.count = basketProduct.getCount();
        this.price = basketProduct.getProduct().getPrice();
    }

    public OrderProduct(BasketProduct basketProduct,int saleRate) {
        this.product = basketProduct.getProduct();
        this.count = basketProduct.getCount();
        this.price = basketProduct.getProduct().getPrice() * (100 - saleRate) / 100;
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public void updateStatus(StatusEnum status) {

        this.status = status;
    }
}
