package com.ariielm.databaselocking.optimisticlock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTestingTheApplication {

    private final OptimisticLockRepository optimisticLockRepository;

    @Transactional
    public Order updateOrder(Order order) {
        try {
            return optimisticLockRepository.updateOrder(order);
        } catch (OptimisticLockException lockException) {
            log.info("Hey! Refresh your page, your update is not up-to-date!");
            throw lockException;
        }
    }

    public void checkMyOrderChanges(Order orderUpdatedFromUser) {
        var order = optimisticLockRepository.getOrder();

        if (order.equals(orderUpdatedFromUser)) {
            log.info("Order is created as expected");
        } else {
            log.info("WAIT! Something is wrong!");
            log.info("Order should be: " + orderUpdatedFromUser);
            log.info("But it is: " + order);
            log.info("Order is not updated as expected! Stop the system!");
            throw new RuntimeException();
        }

    }
}
