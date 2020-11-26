package com.ariielm.databaselocking;

import com.ariielm.databaselocking.optimisticlock.OptimisticLockRepository;
import com.ariielm.databaselocking.optimisticlock.UserTestingTheApplication;
import com.ariielm.databaselocking.pessimisticlock.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;

import static java.lang.Thread.sleep;

@SpringBootApplication
@Slf4j
public class DatabaseLockingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseLockingApplication.class, args);
	}

//	@Bean
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

//	@Bean
	public CommandLineRunner pessimisticLockTest(EntityManagerFactory entityManagerFactory, EntityManager entityManager) {
		return (args) -> {
			EntityManager entityManagerUser1 = entityManagerFactory.createEntityManager();
			EntityManager entityManagerUser2 = entityManagerFactory.createEntityManager();

			Runnable user1 = () -> {
				log.info("[User 1] open the Car System");
				entityManagerUser1.getTransaction().begin();

				log.info("[User 1] getting Car");
				var car = entityManagerUser1.find(Car.class, 1);
//				var car = entityManagerUser1.find(Car.class, 1, LockModeType.PESSIMISTIC_READ);
//				var car = entityManagerUser1.find(Car.class, 1, LockModeType.PESSIMISTIC_WRITE);
				log.info("[User 1] Car gotten: " + car);
				log.info("-----------------------------------");

				log.info("[User 1] updating car");
				car.setDescription("Hyundai Creta");
				log.info("[User 1] Car updated: " + car);
				log.info("-----------------------------------");

				try {
					log.info("[User 1] answers a phone call");
					sleep(3000);
				} catch (InterruptedException e) {
					log.info("[User 1] shut down the phone");
				}

				log.info("[User 1] saving the Car");
				entityManagerUser1.getTransaction().commit();
				log.info("[User 1] Car saved");
			};

			Runnable user2 = () -> {
				try {
					sleep(500);
				} catch (InterruptedException ignored) {}

				log.info("[User 2] open the Car System");
				entityManagerUser2.getTransaction().begin();

				log.info("[User 2] getting Car");
				var car = entityManagerUser2.find(Car.class, 1);
//				var car = entityManagerUser2.find(Car.class, 1, LockModeType.PESSIMISTIC_READ);
//				var car = entityManagerUser2.find(Car.class, 1, LockModeType.PESSIMISTIC_WRITE);
				log.info("[User 2] Car gotten: " + car);
				log.info("-----------------------------------");

				log.info("[User 2] updating car");
				car.setDescription("Nissan Kicks");
				log.info("[User 2] Car updated: " + car);
				log.info("-----------------------------------");

				log.info("[User 2] saving the Car");
				entityManagerUser2.getTransaction().commit();
				log.info("[User 2] Car saved");
			};

			new Thread(user1).start();
			new Thread(user2).start();

			sleep(6000);
			var car = entityManager.find(Car.class, 1);
			log.info("The system is shutting down with the car: " + car);
		};
	}

}
