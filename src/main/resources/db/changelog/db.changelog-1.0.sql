--liquibase formatted sql

--changeset ddevuss:1
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL
    );

--changeset ddevuss:2
CREATE TABLE IF NOT EXISTS locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32),
    user_id INT REFERENCES users (id) ON DELETE CASCADE,
    latitude DECIMAL,
    longitude DECIMAL
);

--changeset ddevuss:3
CREATE TABLE IF NOT EXISTS sessions (
    id VARCHAR PRIMARY KEY,
    user_id INT REFERENCES users (id) ON DELETE CASCADE,
    expires_at TIMESTAMP
);