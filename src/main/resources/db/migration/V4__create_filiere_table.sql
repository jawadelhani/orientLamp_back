CREATE TABLE filiere (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,

                         name VARCHAR(255) NOT NULL,

                         university_id BIGINT NOT NULL,

                         critere_d_admission VARCHAR(255),

                         duration_years INTEGER,

                         tuition_fee DECIMAL(10, 2),

                         admission_type VARCHAR(100),

                         language VARCHAR(50),

                         seats_availabial INTEGER,

                         application_deadline DATE,

                         created_at DATETIME,

                         updated_at DATETIME,

                         CONSTRAINT fk_filiere_university
                             FOREIGN KEY (university_id)
                                 REFERENCES university(id)
) ENGINE=InnoDB;