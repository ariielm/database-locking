package com.ariielm.databaselocking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class DatabaseLockingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseLockingApplication.class, args);
	}

	@Bean
	public CommandLineRunner optimisticLockTest(OptimisticLockRepository optimisticLockRepository, UserTestingTheApplication user1, UserTestingTheApplication user2) {
		return (args) -> {
			log.info("Getting Order");
			var order = optimisticLockRepository.getOrder();
			log.info("Order gotten: " + order);
			log.info("-----------------------------------");

			log.info("User1 updating Order");
			var orderUpdatedFromUser1 = user1.updateOrder(order.toBuilder().description("Description 2").build());
			log.info("Order updated: " + orderUpdatedFromUser1);
			log.info("-----------------------------------");

			log.info("User2 updating Order");
			var orderUpdatedFromUser2 = user2.updateOrder(order.toBuilder().description("Description 3").build());
			log.info("Order updated: " + orderUpdatedFromUser2);
			log.info("-----------------------------------");

			log.info("User1 checking his Order");
			user1.checkMyOrderChanges(orderUpdatedFromUser1);
			log.info("-----------------------------------");

			log.info("User2 checking his Order");
			user2.checkMyOrderChanges(orderUpdatedFromUser2);
			log.info("-----------------------------------");

			log.info("Every user tested the system and it is ok!");

		};
	}

}
