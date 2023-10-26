package com.example.consumer.domain.basketProdcut.repository;

import com.example.consumer.domain.basket.entity.Basket;
import com.example.consumer.domain.basketProdcut.entity.BasketProduct;
import com.example.consumer.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BasketProductRepository extends JpaRepository<BasketProduct, Long> {
    void deleteAllByBasket(Basket basket);
    Optional<BasketProduct> findByBasketAndProduct(Basket basket, Product product);

    @Query("select bp from BasketProduct bp join fetch bp.product where bp.basket.id= :id")
    List<BasketProduct> findByBasketId(@Param("id") Long basketId);
}
