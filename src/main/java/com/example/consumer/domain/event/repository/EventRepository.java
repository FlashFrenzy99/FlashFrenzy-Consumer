package com.example.consumer.domain.event.repository;

import com.example.consumer.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e.product.id from Event e join e.product")
    Set<Long> findProductIdSet();

    @Query("select e from Event e join fetch e.product")
    List<Event> findEventProductList();


    Optional<Event> findByProductId(Long id);
}
