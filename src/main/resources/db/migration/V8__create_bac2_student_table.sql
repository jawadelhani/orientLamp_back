CREATE TABLE bac2_student (
                              id_user BIGINT NOT NULL PRIMARY KEY,

                              diploma_type VARCHAR(100),

                              bac_major VARCHAR(100),

                              institution VARCHAR(255),

                              avg_s1 DECIMAL(4,2),
                              avg_s2 DECIMAL(4,2),
                              avg_s3 DECIMAL(4,2),
                              avg_s4 DECIMAL(4,2),

                              created_at DATETIME,
                              updated_at DATETIME,

                              CONSTRAINT fk_bac2student_user
                                  FOREIGN KEY (id_user)
                                      REFERENCES user(id_user)
) ENGINE=InnoDB;