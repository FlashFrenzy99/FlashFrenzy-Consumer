package com.example.consumer.domain.basket.repository;

import com.example.consumer.domain.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
