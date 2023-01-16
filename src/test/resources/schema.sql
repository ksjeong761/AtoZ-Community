CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL,
    nickname VARCHAR(20) NOT NULL,
    email VARCHAR(320) NOT NULL,
    age INT NOT NULL
);

CREATE TABLE authority (
    id INT AUTO_INCREMENT,
    user_id VARCHAR(20) NOT NULL,
    authority_name VARCHAR(12) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE refresh_token
(
    id          int          NOT NULL AUTO_INCREMENT,
    token_key   varchar(20)  UNIQUE NOT NULL,
    token_value varchar(512) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (token_key)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE posts (
    post_id BIGINT AUTO_INCREMENT,
    user_id VARCHAR(20) NOT NULL,
    title VARCHAR(128) NOT NULL,
    content VARCHAR(4096) NOT NULL,
    hashtags VARCHAR(128) DEFAULT '',
    categories VARCHAR(128) DEFAULT '',
    like_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    comments VARCHAR(512) DEFAULT '',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (post_id),
    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);