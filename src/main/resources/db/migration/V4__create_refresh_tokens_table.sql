CREATE TABLE refresh_tokens (
    token VARCHAR(255) PRIMARY KEY,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_refresh_token_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
