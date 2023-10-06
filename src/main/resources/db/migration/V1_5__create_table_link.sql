CREATE TABLE Url (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      long_url VARCHAR(2048) NOT NULL,
                      short_url VARCHAR(32) NOT NULL UNIQUE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      last_use TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      description VARCHAR(256),
                      user_id BIGINT NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES User (id)
);