package com.example.consumer.domain.event.entity;

import com.example.consumer.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_rate")
    private int saleRate;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Event(Product product, int saleRate) {
        this.product = product;
        this.saleRate = saleRate;
    }
}
