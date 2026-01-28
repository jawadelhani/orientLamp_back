package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.CritereRequestDTO;
import com.example.orientlamp_back.dto.CritereResponseDTO;
import com.example.orientlamp_back.entity.Critere;
import com.example.orientlamp_back.entity.Filiere;
import org.springframework.stereotype.Component;

@Component
public class CritereMapper {

    public Critere toEntity(CritereRequestDTO dto, Filiere filiere) {
        if (dto == null) {
            return null;
        }

        return Critere.builder()
                .filiereId(dto.getFiliereId())
                .filiere(filiere)
                .anneeAcademique(dto.getAnneeAcademique())
                .typeCandidat(dto.getTypeCandidat())
                .serieBacCible(dto.getSerieBacCible())
                .seuilCalcul(dto.getSeuilCalcul())
                .noteConcoursEcrit(dto.getNoteConcoursEcrit())
                .aEntretien(dto.getAEntretien())
                .ageMax(dto.getAgeMax())
                .seuilMatieresSpecifiques(dto.getSeuilMatieresSpecifiques())
                .scorePrepa(dto.getScorePrepa())
                .classementCnc(dto.getClassementCnc())
                .diplomesRequis(dto.getDiplomesRequis())
                .notesSemestres(dto.getNotesSemestres())
                .build();
    }

    public CritereResponseDTO toDTO(Critere entity) {
        if (entity == null) {
            return null;
        }

        return CritereResponseDTO.builder()
                .filiereId(entity.getFiliereId())
                .filiereName(entity.getFiliere() != null ? entity.getFiliere().getName() : null)
                .anneeAcademique(entity.getAnneeAcademique())
                .typeCandidat(entity.getTypeCandidat())
                .serieBacCible(entity.getSerieBacCible())
                .seuilCalcul(entity.getSeuilCalcul())
                .noteConcoursEcrit(entity.getNoteConcoursEcrit())
                .aEntretien(entity.getAEntretien())
                .ageMax(entity.getAgeMax())
                .seuilMatieresSpecifiques(entity.getSeuilMatieresSpecifiques())
                .scorePrepa(entity.getScorePrepa())
                .classementCnc(entity.getClassementCnc())
                .diplomesRequis(entity.getDiplomesRequis())
                .notesSemestres(entity.getNotesSemestres())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(CritereRequestDTO dto, Critere entity, Filiere filiere) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setFiliere(filiere);
        entity.setAnneeAcademique(dto.getAnneeAcademique());
        entity.setTypeCandidat(dto.getTypeCandidat());
        entity.setSerieBacCible(dto.getSerieBacCible());
        entity.setSeuilCalcul(dto.getSeuilCalcul());
        entity.setNoteConcoursEcrit(dto.getNoteConcoursEcrit());
        entity.setAEntretien(dto.getAEntretien());
        entity.setAgeMax(dto.getAgeMax());
        entity.setSeuilMatieresSpecifiques(dto.getSeuilMatieresSpecifiques());
        entity.setScorePrepa(dto.getScorePrepa());
        entity.setClassementCnc(dto.getClassementCnc());
        entity.setDiplomesRequis(dto.getDiplomesRequis());
        entity.setNotesSemestres(dto.getNotesSemestres());
    }
}