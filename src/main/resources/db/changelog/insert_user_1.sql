INSERT INTO user(username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES('user', 1, 1, 1, 1, '{bcrypt}$2a$10$d4kTX6dJ9L6FrJ7H3bCOheCVgsH3/FmFyyhjMdLmwyWfMHPAK8P4.');
INSERT INTO user(username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES('admin', 1, 1, 1, 1, '{bcrypt}$2a$10$d4kTX6dJ9L6FrJ7H3bCOheCVgsH3/FmFyyhjMdLmwyWfMHPAK8P4.');
INSERT INTO user_authority(authority) VALUES('USER');
INSERT INTO user_authority(authority) VALUES('ADMIN');
INSERT INTO user_authorities(user_username, authorities_authority) VALUES('user', 'USER');
INSERT INTO user_authorities(user_username, authorities_authority) VALUES('admin', 'ADMIN');
INSERT INTO round_robin(id, name, user_id) VALUES(1, 'roundrobin', 'user');
ALTER SEQUENCE PUBLIC.HIBERNATE_SEQUENCE RESTART WITH 2;
COMMIT;