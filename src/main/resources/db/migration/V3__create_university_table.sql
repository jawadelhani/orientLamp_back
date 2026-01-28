CREATE TABLE university (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                            name VARCHAR(255) NOT NULL,

                            location VARCHAR(255),

                            type VARCHAR(100),

                            description TEXT,

                            website VARCHAR(255),

                            contact_email VARCHAR(255),

                            phone VARCHAR(20),

                            accreditation_status VARCHAR(100),

                            programs TEXT,

                            created_at DATETIME,

                            updated_at DATETIME
) ENGINE=InnoDB;