--liquibase formatted sql

--changeset ddevuss:1
ALTER TABLE users
    ALTER COLUMN password TYPE VARCHAR(128);