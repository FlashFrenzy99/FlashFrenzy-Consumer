package com.example.consumer.domain.order.repository;

import com.example.consumer.domain.order.entity.Order;
import com.example.consumer.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUser(User user, Pageable pageable);
}
