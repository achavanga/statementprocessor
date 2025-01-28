-- V2__create_statements_report_table.sql
CREATE TABLE report (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        report_id BIGINT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE validation_result (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   reference BIGINT NOT NULL,
                                   description VARCHAR(255),
                                   error_message VARCHAR(255),
                                   report_id BIGINT NOT NULL,
                                   FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE
);