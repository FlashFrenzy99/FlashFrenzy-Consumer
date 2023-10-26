package com.example.consumer.domain.stock.repository;

import com.example.consumer.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface StockRepository extends JpaRepository<Stock, Long> {

//    @Query("select s.product from Stock s where")
//    Product findZeroStockProduct();

//        @Query("select distinct op from OrderProduct op left join Stock s on op.product = s.product where s.stock = 0")
        @Query("select distinct s from Stock s left join OrderProduct op on op.product = s.product where s.stock = 0")
        Set<Stock> findAllWithZeroStock();
}
