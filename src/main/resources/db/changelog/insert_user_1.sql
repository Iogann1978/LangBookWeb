INSERT INTO round_robin(id, name) VALUES(1, 'roundrobin');
INSERT INTO user(login, round_robin_id) VALUES('user', 1);
ALTER SEQUENCE PUBLIC.HIBERNATE_SEQUENCE RESTART WITH 2;
COMMIT;