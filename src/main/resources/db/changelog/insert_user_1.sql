INSERT INTO user(login) VALUES('user');
INSERT INTO round_robin(id, name, user_id) VALUES(1, 'roundrobin', 'user');
ALTER SEQUENCE PUBLIC.HIBERNATE_SEQUENCE RESTART WITH 2;
COMMIT;