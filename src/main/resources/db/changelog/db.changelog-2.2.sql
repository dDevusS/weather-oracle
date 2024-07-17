--liquibase formatted sql

--changeset ddevuss:1
CREATE UNIQUE INDEX ind_user_login ON users (login);