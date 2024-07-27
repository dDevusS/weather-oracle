--liquibase formatted sql

--changeset ddevuss:1
CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(32)  NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL check ( char_length(password) > 5 )

);

--changeset ddevuss:2
CREATE TABLE locations
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(32),
    state     VARCHAR(32),
    user_id   BIGINT REFERENCES users (id) ON DELETE CASCADE,
    latitude  DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

--changeset ddevuss:3
CREATE UNIQUE INDEX ind_user_login ON users (login);
CREATE UNIQUE INDEX idx_base_target ON locations (user_id, latitude, longitude);