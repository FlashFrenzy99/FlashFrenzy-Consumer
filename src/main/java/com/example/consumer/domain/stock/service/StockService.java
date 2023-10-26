package com.example.consumer.domain.stock.service;

import com.example.consumer.domain.stock.entity.Stock;
import com.example.consumer.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long productId, Long stock) {

        Stock findStock = stockRepository.findById(productId).orElseThrow();
        findStock.decrease(stock);
        stockRepository.saveAndFlush(findStock);
    }
}
