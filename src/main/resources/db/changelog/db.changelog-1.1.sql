--liquibase formatted sql

--changeset ddevuss:1
ALTER TABLE locations
    ADD COLUMN state VARCHAR(32);