CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL
    );

CREATE TABLE IF NOT EXISTS locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32),
    user_id INT REFERENCES users (id),
    latitude DECIMAL,
    longitude DECIMAL
);

CREATE TABLE IF NOT EXISTS sessions (
    id VARCHAR PRIMARY KEY,
    user_id INT REFERENCES users (id),
    expires_at TIMESTAMP
);