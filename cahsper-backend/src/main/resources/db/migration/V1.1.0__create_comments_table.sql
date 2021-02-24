/* This file version (V1.1.0) does not relate to the application version. */
CREATE TABLE IF NOT EXISTS comments (
  id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(32) NOT NULL,
  comment VARCHAR(255) NOT NULL,
  created_at BIGINT UNSIGNED DEFAULT 0,
  FOREIGN KEY(user_name) REFERENCES users(name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `comments` ADD INDEX idx_user_name(`user_name`);
