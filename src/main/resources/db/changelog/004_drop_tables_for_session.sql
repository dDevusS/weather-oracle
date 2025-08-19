--liquibase formatted sql

--changeset ddevuss:1
DROP TABLE IF EXISTS spring_session_attributes;

--changeset ddevuss:2
DROP TABLE IF EXISTS spring_session;