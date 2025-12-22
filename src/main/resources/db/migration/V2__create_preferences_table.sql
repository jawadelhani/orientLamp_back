CREATE TABLE preferences (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL UNIQUE,
                             desired_citiest TEXT,
                             budget_range VARCHAR(100),
                             interests TEXT,
                             career_goals TEXT,
                             language_preferences VARCHAR(255),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraint
                             CONSTRAINT fk_preferences_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES `user`(id_user)
                                     ON DELETE CASCADE
)

