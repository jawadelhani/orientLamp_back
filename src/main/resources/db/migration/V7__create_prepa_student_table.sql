CREATE TABLE prepa_student (
                               id_user BIGINT NOT NULL PRIMARY KEY,

                               prepa_major VARCHAR(100),

                               cnc_ranking INTEGER,

                               notes_trimestre TEXT,

                               annee_bac INTEGER,

                               created_at DATETIME,
                               updated_at DATETIME,

                               CONSTRAINT fk_prepastudent_user
                                   FOREIGN KEY (id_user)
                                       REFERENCES user(id_user)
) ENGINE=InnoDB;