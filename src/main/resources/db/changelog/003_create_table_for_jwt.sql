--liquibase formatted sql

--changeset ddevuss:1
CREATE TABLE jwt_refresh_tokens
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token_hash VARCHAR(128) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    revoked BOOLEAN NOT NULL DEFAULT FALSE
);

--changeset ddevuss:2
CREATE INDEX idx_refresh_token_user_id ON jwt_refresh_tokens(user_id);
CREATE INDEX idx_refresh_token_hash ON jwt_refresh_tokens(token_hash);
CREATE INDEX idx_refresh_token_expires_at ON jwt_refresh_tokens(expires_at);
CREATE INDEX idx_refresh_token_revoked ON jwt_refresh_tokens(revoked);