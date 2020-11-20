# Database locking

## Optimistic locking

Just by adding a new field with the @Version annotation to the Entity is enough to the Hibernate create a new field on the table that will be used as the register version control.

On this project, when running the main class (DatabaseApplication), it is possible to test the Optimistic Lock. It is possible to test with the field _version_ on the Order entity and without it.

### Scenarios:

* **Without the version field on the Order entity**: the user1 and the user2 gets an order an tries to updated it.
The user1 will update the order, and the user2 will updated it too, but only the changes made by the user2 will be kept on the order.
When both the users check the order, the user1 will notice an inconsistency on the order and a message will be logged: _"Order is not updated as expected! Stop the system!"_

* **With the version field on the Order entity**: the user1 and the user2 gets an order an tries to updated it.
The user1 will update the order, and the user2 will try to updated it too, but with the version control made by the optimistic lock,
 the different version will be detected, not updating the register, logging a message _"Hey! Refresh your page, your update is not up-to-date!"_, and an OptimisticLockException will be thrown.

### Testing the scenarios:

* **Without the version field on the Order entity**: Uncomment the method optimisticLockTest on the DatabaseApplication; the data.sql must contain only an insert without the version field, and the Order entity must contain the version field commented.

* **With the version field on the Order entity**: Uncomment the method optimisticLockTest on the DatabaseApplication; the data.sql must contain only an insert with the version field, and the Order entity must contain the version field uncommented.