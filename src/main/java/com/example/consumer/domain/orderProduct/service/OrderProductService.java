package com.example.consumer.domain.orderProduct.service;

import com.example.consumer.domain.orderProduct.entity.OrderProduct;
import com.example.consumer.domain.orderProduct.entity.StatusEnum;
import com.example.consumer.domain.orderProduct.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    @Transactional
    public void updateStatus(Long id, StatusEnum statusEnum) {
        OrderProduct orderProduct = orderProductRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 주문 상품이 존재하지 않습니다.")
        );
        orderProduct.updateStatus(statusEnum);
    }
}
