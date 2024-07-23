--liquibase formatted sql

--changeset ddevuss:1
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL
    );

--changeset ddevuss:2
CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(32),
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

--changeset ddevuss:3
CREATE UNIQUE INDEX ind_user_login ON users (login);