INSERT INTO user(username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES('dicts', 1, 1, 1, 1, '{bcrypt}$2a$10$2/LnQqEgh9qPBT3fL5tB6e5aNZ5jzkopWCZr27VTwhPPkqx0XH8/O');
INSERT INTO user_authority(username, authority) VALUES('dicts', 'USER');
INSERT INTO round_robin(id, name, username) VALUES(611, 'roundrobin', 'dicts');
ALTER SEQUENCE PUBLIC.HIBERNATE_SEQUENCE RESTART WITH 612;
COMMIT;