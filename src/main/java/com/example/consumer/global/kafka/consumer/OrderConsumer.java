package com.example.consumer.global.kafka.consumer;

import com.example.consumer.domain.event.repository.EventRepository;
import com.example.consumer.domain.orderProduct.entity.OrderProduct;
import com.example.consumer.domain.orderProduct.entity.StatusEnum;
import com.example.consumer.domain.orderProduct.service.OrderProductService;
import com.example.consumer.domain.stock.service.StockService;
import com.example.consumer.global.redis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;
    private final RedisRepository redisRepository;
    private final StockService stockService;
    private final EventRepository eventRepository;
    private final OrderProductService orderProductService;

    @KafkaListener(topics = "order-2", groupId = "group_1")
    public void listener(ConsumerRecord data) throws JsonProcessingException {
        log.debug(data.toString());
        long startTime = System.currentTimeMillis();
        List<OrderProduct> orderProductList = Arrays.asList(objectMapper.readValue(data.value().toString(), OrderProduct[].class));
        Set<Long> eventIdList = getEventIdSet();
        for (OrderProduct orderProduct : orderProductList) {
            long startTime2 = System.currentTimeMillis();


            Long productId = orderProduct.getProduct().getId();
            RLock lock = redissonClient.getLock("product" + productId);
            try {
                boolean available = lock.tryLock(1, 3, TimeUnit.SECONDS);
                if (!available) {
                    log.error("주문 시도 중 lock 획득 실패");
                    orderProductService.updateStatus(orderProduct.getId(), StatusEnum.FAIL);
                    continue;
                }

                log.debug("락 elapsed time : "  + (System.currentTimeMillis() - startTime2) + "ms.");
                startTime2 = System.currentTimeMillis();
                stockService.decrease(productId, orderProduct.getCount());
                log.debug("재고 감소 elapsed time : "  + (System.currentTimeMillis() - startTime2) + "ms.");
                startTime2 = System.currentTimeMillis();
                if (eventIdList.contains(productId)) {
                    redisRepository.decrement("product:sale:" + productId + ":stock", orderProduct.getCount());
                }
                orderProductService.updateStatus(orderProduct.getId(), StatusEnum.SUCCESS);
                log.debug("성공 처리 elapsed time : "  + (System.currentTimeMillis() - startTime2) + "ms.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                log.error("재고 부족으로 구매 실패");
                orderProductService.updateStatus(orderProduct.getId(), StatusEnum.FAIL);
            } finally {
                lock.unlock();
            }
        }
        log.debug("컨슈머 재고 처리 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");


    }

    private Set<Long> getEventIdSet() {
        Set<Long> eventIdList;
        String eventIdListString = redisRepository.getValue("product:sale:list");
        if (eventIdListString != null) {
            try {
                Long[] list = objectMapper.readValue(eventIdListString, Long[].class);
                eventIdList = new HashSet<>(List.of(list));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            eventIdList = eventRepository.findProductIdSet();
        }
        return eventIdList;
    }
}
