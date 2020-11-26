package com.ariielm.databaselocking.optimisticlock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class OptimisticLockRepository {

    private final EntityManager em;

    public Order getOrder() {
        return em.find(Order.class, 1);
    }

    public Order updateOrder(Order order) {
        return em.merge(order);
    }

}
