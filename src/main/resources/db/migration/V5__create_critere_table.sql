CREATE TABLE critere (
                         filiere_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         annee_academique VARCHAR(20) NOT NULL,
                         type_candidat VARCHAR(100) NOT NULL,
                         serie_bac_cible VARCHAR(100),
                         seuil_calcul DECIMAL(5,2),
                         note_concours_ecrit DECIMAL(5,2),
                         a_entretien BOOLEAN DEFAULT FALSE,
                         age_max INT,
                         seuil_matieres_specifiques TEXT,
                         score_prepa DECIMAL(5,2),
                         classement_cnc INT,
                         diplomes_requis TEXT,
                         notes_semestres TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE critere
    ADD CONSTRAINT fk_critere_filiere
        FOREIGN KEY (filiere_id) REFERENCES filiere(id) ON DELETE CASCADE;

