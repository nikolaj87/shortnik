CREATE TABLE User (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(32) UNIQUE NOT NULL,
                      password_hash CHAR(60) NOT NULL,
                      email VARCHAR(64) UNIQUE NOT NULL,
                      registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);