CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    nickname VARCHAR(50),
    email VARCHAR(50)
);
