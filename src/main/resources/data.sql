-- Without the version
--INSERT INTO ORDER_TABLE (DESCRIPTION) VALUES ('Description 1');

-- With version to Optimistic Lock
INSERT INTO ORDER_TABLE (DESCRIPTION, VERSION) VALUES ('Description 1', 0);

-- Register to test Pessimistic Lock
INSERT INTO CAR (DESCRIPTION) VALUES ('Ford EcoSport');