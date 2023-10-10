CREATE TABLE Role (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(8) UNIQUE NOT NULL
);
INSERT INTO Role (name) VALUES ('admin');
INSERT INTO Role (name) VALUES ('owner');
INSERT INTO Role (name) VALUES ('user');