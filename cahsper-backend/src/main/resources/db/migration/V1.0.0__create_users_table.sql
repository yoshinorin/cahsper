/* This file version (V1.0.0) does not relate to the application version. */
CREATE TABLE IF NOT EXISTS users (
  name VARCHAR(32) UNIQUE NOT NULL,
  created_at BIGINT UNSIGNED DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
