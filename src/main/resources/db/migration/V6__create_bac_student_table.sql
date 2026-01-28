CREATE TABLE bac_student (
                             id_user BIGINT NOT NULL PRIMARY KEY,
                             bac_major VARCHAR(100),
                             bac_degree VARCHAR(100),
                             grade DECIMAL(5,2),
                             subject_degree VARCHAR(100),
                             bac_year_graduation INTEGER,
                             created_at DATETIME,
                             updated_at DATETIME,


                             CONSTRAINT fk_bacstudent_user
                                 FOREIGN KEY (id_user)
                                     REFERENCES `user`(id_user)
) ENGINE=InnoDB;