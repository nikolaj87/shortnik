CREATE TABLE Role_User
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (role_id) REFERENCES Role (id)
);