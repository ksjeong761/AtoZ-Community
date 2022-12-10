CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL,
    nickname VARCHAR(20) NOT NULL,
    email VARCHAR(320) NOT NULL
);

CREATE TABLE authority (
    id INT AUTO_INCREMENT,
    user_id VARCHAR(20) NOT NULL,
    authority_name VARCHAR(12) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE refresh_token
(
    id          int          NOT NULL AUTO_INCREMENT,
    token_key   varchar(20)  UNIQUE NOT NULL,
    token_value varchar(512) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (token_key) REFERENCES users(user_id)
);

CREATE TABLE posts (
	post_id INT AUTO_INCREMENT,
    user_id VARCHAR(20),
    title VARCHAR(128),
    content VARCHAR(4096),
    like_count INT,
    view_count INT,
    comments VARCHAR(512),
    created_at DATETIME,
    updated_at DATETIME,

	PRIMARY KEY (post_id),
	FOREIGN KEY (user_id) REFERENCES users(user_id)
);