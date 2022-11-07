CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) UNIQUE NOT NULL,
    password BINARY(32) NOT NULL,
    nickname VARCHAR(20) NOT NULL,
    email VARCHAR(320) NOT NULL,
    password_salt BINARY(32) NOT NULL
);
