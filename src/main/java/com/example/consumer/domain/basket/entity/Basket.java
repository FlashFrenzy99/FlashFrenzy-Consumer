package com.example.consumer.domain.basket.entity;

import com.example.consumer.domain.basketProdcut.entity.BasketProduct;
import com.example.consumer.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Basket {

    @Id
    @Column(name = "basket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "basket",fetch = FetchType.LAZY) // DEAFAULT 는 LAZY이나 EAGER로 수정해놨음
    private List<BasketProduct> list = new ArrayList<>();
}