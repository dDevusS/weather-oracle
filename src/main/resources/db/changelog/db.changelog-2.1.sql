--liquibase formatted sql

--changeset ddevuss:1
ALTER TABLE users
    ALTER COLUMN id TYPE BIGINT;

--changeset ddevuss:2
ALTER TABLE locations
    ALTER COLUMN id TYPE BIGINT;

--changeset ddevuss:3
ALTER TABLE locations
    ALTER COLUMN user_id TYPE BIGINT;

--changeset ddevuss:4
ALTER TABLE locations
    ALTER COLUMN latitude TYPE DOUBLE PRECISION;

--changeset ddevuss:5
ALTER TABLE locations
    ALTER COLUMN longitude TYPE DOUBLE PRECISION;