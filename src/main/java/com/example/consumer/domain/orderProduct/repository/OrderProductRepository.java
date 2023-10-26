package com.example.consumer.domain.orderProduct.repository;

import com.example.consumer.domain.orderProduct.entity.OrderProduct;
import com.example.consumer.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    //수정 필요
//    @Query("select distinct op from OrderProduct op join fetch op.product where op.product = 0")
    @Query("select distinct op from OrderProduct op left join Stock s on op.product = s.product where s.stock = 0")
    List<OrderProduct> findAllWithZeroStock();



    @Query("SELECT op.product FROM OrderProduct op GROUP BY op.product.id ORDER BY COUNT(op.product.id) DESC limit 5")
    List<Product> findTop5Order();
}
