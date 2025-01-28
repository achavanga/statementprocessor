-- V1__create_statements_table.sql
CREATE TABLE Statement (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           reference BIGINT,
                           account_number VARCHAR(255) NOT NULL,
                           start_balance DECIMAL(10, 2),
                           mutation DECIMAL(10, 2),
                           description VARCHAR(255),
                           end_balance DECIMAL(10, 2),
                           UNIQUE (reference)
);