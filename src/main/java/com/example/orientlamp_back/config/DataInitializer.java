package com.example.orientlamp_back.config;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.orientlamp_back.entity.Critere;
import com.example.orientlamp_back.entity.Filiere;
import com.example.orientlamp_back.entity.University;
import com.example.orientlamp_back.repository.CritereRepository;
import com.example.orientlamp_back.repository.FiliereRepository;
import com.example.orientlamp_back.repository.UniversityRepository;
import com.example.orientlamp_back.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Seeds the database with real Moroccan higher-education institutions
 * on every application start-up — but ONLY if the university table is empty.
 * This makes it fully idempotent; removing a school from this list and
 * wiping the DB is enough to change the seed data.
 */
/**
 * =====================================================================
 *  ORIENTLAMP — MOROCCAN HIGHER-EDUCATION SEED DATA
 * =====================================================================
 *
 * Single source of truth for schools, programmes and admission criteria.
 *
 * TO ADD / EDIT A SCHOOL:
 *   1. Add or update a SchoolSeed entry in ALL_SCHOOLS below.
 *   2. Rebuild and restart the app with  app.seed.force=true  (set it
 *      once, then revert to false so subsequent boots skip re-seeding).
 *
 * IMAGE STRATEGY:
 *   Each school gets a branded SVG emblem auto-generated on first boot
 *   and stored under /uploads/universities/{id}/logo.svg — served
 *   from this very server. No broken external URLs, no CDN needed.
 *   An admin can later replace it via POST /api/universities/{id}/image.
 *
 * COLOR LEGEND  (logoColor):
 *   Grande École Publique  → #0D47A1 (deep blue)
 *   ENSA family            → #BF360C (deep orange)
 *   Commerce / ENCG        → #6A1B9A (purple)
 *   Université Publique    → #1B5E20 (dark green)
 *   Institut / Spécialisé  → #E65100 (amber-orange)
 *   Privée                 → #C62828 (dark red)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UniversityRepository  universityRepository;
    private final FiliereRepository     filiereRepository;
    private final CritereRepository     critereRepository;
    private final FileStorageService    fileStorageService;

    // Self-injection so @Transactional on seedOne() is applied via Spring proxy
    @Lazy @Autowired
    private DataInitializer self;

    @Value("${app.seed.force:false}")
    private boolean forceReseed;

    // ------------------------------------------------------------------ //
    //  Entry point                                                         //
    // ------------------------------------------------------------------ //

    @Override
    public void run(ApplicationArguments args) {
        if (!forceReseed && universityRepository.count() > 0) {
            log.info("DataInitializer: data already present — skipping. " +
                     "Set app.seed.force=true to force a full reseed.");
            return;
        }

        if (forceReseed) {
            log.warn("DataInitializer: force reseed — wiping existing data.");
            critereRepository.deleteAll();
            filiereRepository.deleteAll();
            universityRepository.deleteAll();
        }

        log.info("DataInitializer: seeding {} Moroccan institutions…", ALL_SCHOOLS.size());
        int seeded = 0;
        for (SchoolSeed seed : ALL_SCHOOLS) {
            try {
                self.seedOne(seed);
                seeded++;
            } catch (Exception e) {
                log.error("DataInitializer: failed to seed '{}': {}", seed.name(), e.getMessage(), e);
            }
        }
        log.info("DataInitializer: {} institutions seeded.", seeded);
    }

    // ------------------------------------------------------------------ //
    //  Per-school logic                                                    //
    // ------------------------------------------------------------------ //

    @Transactional
    public void seedOne(SchoolSeed s) {
        Instant now = Instant.now();

        // 1. Persist university (no imageUrl yet — need the DB-generated ID first)
        String[] ex = SCHOOL_EXTRAS.getOrDefault(s.name(), new String[4]);
        University university = universityRepository.save(University.builder()
                .name(s.name())
                .location(s.location())
                .type(s.type())
                .description(s.description())
                .website(s.website())
                .contactEmail(s.contactEmail())
                .phone(s.phone())
                .accreditationStatus(s.accreditationStatus())
                .programs(s.programs())
                .slug(ex[0])
                .headerImageUrl(ex[1])
                .earthViewUrl(ex[2])
                .galleryImages(ex[3])
                .createdAt(now)
                .updatedAt(now)
                .build());

        // 2. Generate SVG logo served from our own server
        String imageUrl = fileStorageService.generateAndStoreSvgLogo(
                university.getId(), s.logoAbbr(), s.logoColor());
        if (imageUrl != null) {
            university.setImageUrl(imageUrl);
            university.setUpdatedAt(Instant.now());
            universityRepository.save(university);
        }

        // 3. Persist each filière + its critère
        for (FiliereSeed fs : s.filieres()) {
            Filiere savedF = filiereRepository.save(Filiere.builder()
                    .name(fs.name())
                    .university(university)
                    .critereAdmission(fs.critereAdmission())
                    .durationYears(fs.durationYears())
                    .tuitionFee(fs.tuitionFee())
                    .admissionType(fs.admissionType())
                    .language(fs.language())
                    .seatsAvailabial(fs.seats())
                    .applicationDeadline(fs.applicationDeadline())
                    .build());

            if (fs.hasCritere()) {
                critereRepository.save(Critere.builder()
                        .filiere(savedF)
                        .anneeAcademique(fs.anneeAcademique())
                        .typeCandidat(fs.typeCandidat())
                        .serieBacCible(fs.serieBacCible())
                        .seuilCalcul(fs.seuilCalcul())
                        .noteConcoursEcrit(fs.noteConcoursEcrit())
                        .aEntretien(fs.aEntretien())
                        .ageMax(fs.ageMax())
                        .scorePrepa(fs.scorePrepa())
                        .classementCnc(fs.classementCnc())
                        .diplomesRequis(fs.diplomesRequis())
                        .notesSemestres(fs.notesSemestres())
                        .build());
            }
        }
    }

    // ================================================================== //
    //  Inner records (data models)                                        //
    // ================================================================== //

    private record SchoolSeed(
            String name, String location, String type,
            String description, String website,
            String contactEmail, String phone,
            String accreditationStatus, String programs,
            String logoAbbr, String logoColor,
            List<FiliereSeed> filieres) {}

    private record FiliereSeed(
            String name, String critereAdmission,
            int durationYears, BigDecimal tuitionFee,
            String admissionType, String language,
            int seats, LocalDate applicationDeadline,
            // critère fields
            boolean hasCritere,
            String anneeAcademique, String typeCandidat, String serieBacCible,
            BigDecimal seuilCalcul, BigDecimal noteConcoursEcrit,
            boolean aEntretien, Integer ageMax,
            BigDecimal scorePrepa, Integer classementCnc,
            String diplomesRequis, String notesSemestres) {}

    // ------------------------------------------------------------------ //
    //  Builder helpers                                                     //
    // ------------------------------------------------------------------ //

    private static LocalDate d(int month, int day) {
        return LocalDate.of(2026, month, day);
    }

    private static BigDecimal bd(double v) {
        return BigDecimal.valueOf(v);
    }

    /** Filière via CNC (CPGE → Grande École) */
    private static FiliereSeed cnc(String name, int seats, BigDecimal seuil, Integer classement) {
        return new FiliereSeed(name,
                "Sélection via le Concours National Commun (CNC). Réservé aux lauréats des Classes Préparatoires (MP, PC, PSI, TSI, PT).",
                3, BigDecimal.ZERO, "CNC", "Français", seats, d(6, 15),
                true, "2025-2026", "Lauréat CPGE", "Sciences Mathématiques",
                seuil, null, false, 28, scorePrepa(classement), classement,
                "Diplôme de Classes Préparatoires scientifiques", null);
    }

    /** Filière concours national post-bac */
    private static FiliereSeed concoursPostBac(String name, int seats,
            String serieBac, BigDecimal seuil, boolean entretien) {
        return new FiliereSeed(name,
                "Admission sur concours national post-baccalauréat. Épreuve écrite + oral.",
                5, BigDecimal.ZERO, "CONCOURS", "Français", seats, d(5, 31),
                true, "2025-2026", "Bacheliers", serieBac,
                seuil, bd(12.0), entretien, 22,
                null, null, "Baccalauréat marocain sciences", null);
    }

    /** Filière sur dossier (Master, MBA, etc.) */
    private static FiliereSeed dossier(String name, int durationYears,
            BigDecimal fee, int seats, String diplomesRequis) {
        return new FiliereSeed(name,
                "Sélection sur dossier académique et entretien de motivation.",
                durationYears, fee, "DOSSIER", "Français/Arabe", seats, d(6, 30),
                true, "2025-2026", "Titulaires d'un diplôme supérieur", null,
                bd(12.0), null, true, null, null, null,
                diplomesRequis, null);
    }

    /** Simple filière (no critère detail — post-bac dossier, bulk schools) */
    private static FiliereSeed simple(String name, int durationYears,
            BigDecimal fee, String admissionType, String language, int seats) {
        return new FiliereSeed(name,
                "Sélection sur dossier académique (résultats du baccalauréat et notes lycée).",
                durationYears, fee, admissionType, language, seats, d(7, 31),
                false, null, null, null, null,
                null, false, null, null, null, null, null);
    }

    private static BigDecimal scorePrepa(Integer classement) {
        if (classement == null) return null;
        if (classement <= 100)  return bd(17.5);
        if (classement <= 300)  return bd(16.0);
        if (classement <= 600)  return bd(14.5);
        if (classement <= 1200) return bd(13.0);
        return bd(12.0);
    }

    // ================================================================== //
    //  *** CANONICAL SCHOOL DATABASE ***                                  //
    //  This is the single file to edit when updating school data.         //
    // ================================================================== //

    private static final List<SchoolSeed> ALL_SCHOOLS = List.of(

        // ==============================================================
        //  GRANDES ÉCOLES D'INGÉNIEURS — voie CNC (CPGE)
        // ==============================================================

        new SchoolSeed(
            "École Mohammadia d'Ingénieurs (EMI)",
            "Rabat", "Grande École Publique",
            "La plus ancienne et la plus prestigieuse école d'ingénieurs du Maroc, fondée en 1959 et rattachée à l'Université Mohammed V. " +
            "Elle forme des ingénieurs d'État reconnus dans tout le Maghreb et au-delà, avec plus de 10 000 lauréats dans les secteurs public et privé.",
            "http://www.emi.ac.ma", "contact@emi.ac.ma", "+212 537 68 00 10",
            "Accréditée",
            "Génie Civil, Informatique, Génie Électrique, Génie Mécanique, Topographie, Génie Industriel",
            "EMI", "#0D47A1",
            List.of(
                cnc("Génie Informatique", 90, bd(14.50), 400),
                cnc("Génie Civil", 80, bd(13.80), 600),
                cnc("Génie Électrique", 70, bd(14.20), 500),
                cnc("Génie Mécanique", 60, bd(13.50), 700),
                cnc("Topographie", 40, bd(13.00), 900)
            )
        ),

        new SchoolSeed(
            "Institut National des Postes et Télécommunications (INPT)",
            "Rabat", "Grande École Publique",
            "École d'ingénieurs d'élite spécialisée dans les télécommunications, les réseaux, la cybersécurité et les nouvelles technologies. " +
            "Rattachée au Ministère du Numérique, l'INPT forme les futurs leaders de l'écosystème digital marocain.",
            "https://inpt.ac.ma", "contact@inpt.ac.ma", "+212 537 77 30 70",
            "Accréditée",
            "Télécommunications, Réseaux, Cybersécurité, Intelligence Artificielle, Cloud Computing",
            "INPT", "#1A237E",
            List.of(
                cnc("Ingénierie des Réseaux et Télécommunications", 80, bd(15.00), 250),
                cnc("Ingénierie des Systèmes Informatiques", 70, bd(15.20), 220),
                cnc("Cybersécurité et Confiance Numérique", 50, bd(15.50), 180)
            )
        ),

        new SchoolSeed(
            "École Nationale Supérieure d'Informatique et d'Analyse des Systèmes (ENSIAS)",
            "Rabat", "Grande École Publique",
            "École d'ingénieurs de l'Université Mohammed V, référence nationale en génie logiciel, intelligence artificielle et systèmes d'information. " +
            "Fondée en 1992, l'ENSIAS entretient des partenariats avec les plus grandes entreprises tech mondiales.",
            "https://ensias.um5.ac.ma", "contact@ensias.um5.ac.ma", "+212 537 68 00 30",
            "Accréditée",
            "Génie Logiciel, Intelligence Artificielle, Systèmes d'Information, Sécurité Informatique, Big Data",
            "ENSIAS", "#1565C0",
            List.of(
                cnc("Génie Logiciel et Systèmes d'Information", 80, bd(15.30), 200),
                cnc("Intelligence Artificielle et Sciences des Données", 60, bd(15.80), 150),
                cnc("Sécurité des Systèmes d'Information", 50, bd(15.00), 230),
                cnc("Ingénierie du Web et du Mobile", 60, bd(14.80), 280)
            )
        ),

        new SchoolSeed(
            "École Hassania des Travaux Publics (EHTP)",
            "Casablanca", "Grande École Publique",
            "Placée sous la tutelle du Ministère de l'Équipement, l'EHTP est la référence marocaine en génie civil, hydraulique et infrastructures. " +
            "Ses lauréats pilotent les plus grands projets d'infrastructure du pays (autoroutes, barrages, villes nouvelles).",
            "http://www.ehtp.ac.ma", "info@ehtp.ac.ma", "+212 522 23 83 00",
            "Accréditée",
            "Génie Civil, Géotechnique, Hydraulique et Environnement, Transport et Logistique, Géomatique",
            "EHTP", "#006064",
            List.of(
                cnc("Génie Civil et Structures", 70, bd(13.80), 600),
                cnc("Hydraulique et Environnement", 50, bd(13.50), 700),
                cnc("Transport et Mobilité Durable", 40, bd(13.20), 850),
                cnc("Géomatique et SIG", 30, bd(13.00), 950)
            )
        ),

        new SchoolSeed(
            "École Nationale Supérieure d'Électricité et de Mécanique (ENSEM)",
            "Casablanca", "Grande École Publique",
            "Rattachée à l'Université Hassan II, l'ENSEM forme des ingénieurs d'État en génie électrique, automatique, énergétique et mécanique industrielle. " +
            "Fortement ancrée dans le tissu industriel casablancais, elle dispose de laboratoires de pointe.",
            "https://ensem.univh2c.ma", "contact@ensem.univh2c.ma", "+212 522 23 32 00",
            "Accréditée",
            "Génie Électrique, Automatique et Contrôle, Génie Mécanique, Énergétique et Développement Durable",
            "ENSEM", "#1B5E20",
            List.of(
                cnc("Génie Électrique et Systèmes Embarqués", 70, bd(14.00), 550),
                cnc("Génie Mécanique et Productique", 60, bd(13.50), 700),
                cnc("Énergétique et Développement Durable", 50, bd(13.20), 800)
            )
        ),

        new SchoolSeed(
            "École Nationale de l'Industrie Minérale (ENIM)",
            "Rabat", "Grande École Publique",
            "L'ENIM forme des ingénieurs spécialisés dans les mines, la géologie, la métallurgie et les matériaux. " +
            "Elle contribue directement au développement du secteur minier marocain, 3ème producteur mondial de phosphate.",
            "https://www.enim.ac.ma", "contact@enim.ac.ma", "+212 537 68 62 40",
            "Accréditée",
            "Mines, Géologie, Métallurgie, Géotechnique Minière, Matériaux et Ingénierie des Surfaces",
            "ENIM", "#4E342E",
            List.of(
                cnc("Mines et Géologie", 50, bd(13.00), 900),
                cnc("Métallurgie et Science des Matériaux", 40, bd(12.80), 1000),
                cnc("Géotechnique et Environnement Minier", 30, bd(12.50), 1100)
            )
        ),

        new SchoolSeed(
            "École Nationale Supérieure des Mines de Rabat (ENSMR)",
            "Rabat", "Grande École Publique",
            "L'ENSMR offre une formation d'ingénieur d'État orientée vers les industries minières, pétrolières et les géosciences. " +
            "Elle entretient d'étroits partenariats avec l'OCP, principale entreprise minière du Maroc.",
            "https://www.ensmr.ac.ma", "contact@ensmr.ac.ma", "+212 537 77 17 97",
            "Accréditée",
            "Sciences et Génie des Matériaux, Pétrole et Gaz, Géosciences Appliquées",
            "ENSMR", "#37474F",
            List.of(
                cnc("Sciences et Génie des Matériaux", 40, bd(12.80), 1000),
                cnc("Géosciences Appliquées et Pétrole", 30, bd(12.50), 1100)
            )
        ),

        new SchoolSeed(
            "École Nationale Forestière des Ingénieurs (ENFI)",
            "Salé", "Grande École Publique",
            "Seule école d'ingénieurs forestiers du Maroc, l'ENFI forme des cadres spécialisés dans la gestion durable des forêts, " +
            "la conservation de la biodiversité et l'aménagement des ressources naturelles.",
            "https://www.enfi.ac.ma", "contact@enfi.ac.ma", "+212 537 88 21 46",
            "Accréditée",
            "Eaux et Forêts, Lutte Contre la Désertification, Gestion de la Faune Sauvage, Génie de l'Environnement",
            "ENFI", "#2E7D32",
            List.of(
                concoursPostBac("Ingénierie des Eaux et Forêts", 50, "Sciences de la Vie et de la Terre", bd(14.00), false),
                concoursPostBac("Génie de l'Environnement et Développement Durable", 30, "Sciences Mathématiques", bd(13.50), false)
            )
        ),

        // ==============================================================
        //  ENSA — ÉCOLES NATIONALES DES SCIENCES APPLIQUÉES
        // ==============================================================

        new SchoolSeed(
            "École Nationale des Sciences Appliquées de Marrakech (ENSA Marrakech)",
            "Marrakech", "Grande École Publique",
            "Rattachée à l'Université Cadi Ayyad, l'ENSA Marrakech offre une formation pluridisciplinaire en ingénierie. " +
            "Son campus moderne au cœur de la ville ocre bénéficie d'un environnement académique riche.",
            "http://ensa.uca.ma", "contact@ensa.uca.ma", "+212 524 43 36 03",
            "Accréditée",
            "Génie Informatique, Génie Civil, Génie Électrique, Génie Mécanique, Génie Industriel",
            "ENSA", "#E65100",
            List.of(
                concoursPostBac("Génie Informatique", 120, "Sciences Mathématiques", bd(15.00), false),
                concoursPostBac("Génie Électrique et Informatique Industrielle", 80, "Sciences Mathématiques", bd(14.50), false),
                concoursPostBac("Génie Civil et Environnement", 80, "Sciences Mathématiques", bd(14.00), false)
            )
        ),

        new SchoolSeed(
            "École Nationale des Sciences Appliquées d'Agadir (ENSA Agadir)",
            "Agadir", "Grande École Publique",
            "Rattachée à l'Université Ibn Zohr, l'ENSA Agadir propose des formations en ingénierie avec une forte orientation " +
            "vers les technologies durables et les besoins spécifiques de la région du Souss-Massa.",
            "https://ensa.uiz.ac.ma", "contact@ensa.uiz.ac.ma", "+212 528 24 08 29",
            "Accréditée",
            "Génie Informatique, Génie Civil, Génie Électrique, Agro-industrie, Halieutique",
            "ENSA", "#BF360C",
            List.of(
                concoursPostBac("Génie Informatique et Systèmes Intelligents", 100, "Sciences Mathématiques", bd(14.80), false),
                concoursPostBac("Génie Industriel et Logistique", 80, "Sciences Mathématiques", bd(14.20), false)
            )
        ),

        new SchoolSeed(
            "École Nationale des Sciences Appliquées de Fès (ENSA Fès)",
            "Fès", "Grande École Publique",
            "Rattachée à l'Université USMBA, l'ENSA Fès est reconnue pour la qualité de son corps enseignant et la diversité de ses filières, " +
            "dans une ville universitaire historique à forte tradition académique.",
            "https://ensa.usmba.ac.ma", "contact@ensa.usmba.ac.ma", "+212 535 61 82 03",
            "Accréditée",
            "Génie Informatique, Génie Électrique, Génie Industriel, Génie Civil",
            "ENSA", "#D84315",
            List.of(
                concoursPostBac("Génie Informatique", 120, "Sciences Mathématiques", bd(15.00), false),
                concoursPostBac("Génie Électrique et Automatique", 80, "Sciences Mathématiques", bd(14.50), false)
            )
        ),

        new SchoolSeed(
            "École Nationale des Sciences Appliquées de Tanger (ENSA Tanger)",
            "Tanger", "Grande École Publique",
            "Rattachée à l'Université Abdelmalek Essaadi, l'ENSA Tanger se trouve au cœur d'un hub économique stratégique. " +
            "Ses filières s'orientent vers les besoins industriels de la région Tanger-Tétouan.",
            "https://ensa.uae.ac.ma", "contact@ensa.uae.ac.ma", "+212 539 39 39 55",
            "Accréditée",
            "Génie Informatique, Génie Civil, Génie Électrique, Génie Logistique",
            "ENSA", "#E64A19",
            List.of(
                concoursPostBac("Génie Informatique et Systèmes Distribués", 120, "Sciences Mathématiques", bd(14.80), false),
                concoursPostBac("Génie Industriel et Logistique des Flux", 80, "Sciences Mathématiques", bd(14.20), false)
            )
        ),

        new SchoolSeed(
            "École Nationale des Sciences Appliquées d'Oujda (ENSA Oujda)",
            "Oujda", "Grande École Publique",
            "Rattachée à l'Université Mohammed Premier, l'ENSA Oujda dessert la région de l'Oriental et prépare des ingénieurs aux défis " +
            "de développement d'une région en forte croissance.",
            "https://ensa.ump.ma", "contact@ensa.ump.ma", "+212 536 50 06 21",
            "Accréditée",
            "Génie Informatique, Génie Électrique, Génie Industriel",
            "ENSA", "#FF3D00",
            List.of(
                concoursPostBac("Génie Informatique", 100, "Sciences Mathématiques", bd(14.50), false),
                concoursPostBac("Génie Électrique", 80, "Sciences Mathématiques", bd(14.00), false)
            )
        ),

        new SchoolSeed(
            "École Nationale des Sciences Appliquées de Kénitra (ENSA Kénitra)",
            "Kénitra", "Grande École Publique",
            "Rattachée à l'Université Ibn Tofail, l'ENSA Kénitra profite du tissu industriel de la région Rabat-Salé-Kénitra " +
            "pour offrir des formations en ingénierie directement connectées au marché de l'emploi local.",
            "https://ensa.uit.ac.ma", "contact@ensa.uit.ac.ma", "+212 537 37 76 52",
            "Accréditée",
            "Génie Informatique, Génie Civil, Génie Électrique, Génie Industriel",
            "ENSA", "#FF6D00",
            List.of(
                concoursPostBac("Génie Informatique et Réseaux", 100, "Sciences Mathématiques", bd(14.50), false),
                concoursPostBac("Génie Industriel", 80, "Sciences Mathématiques", bd(14.00), false)
            )
        ),

        // ==============================================================
        //  ÉCOLES SPÉCIALISÉES
        // ==============================================================

        new SchoolSeed(
            "Institut Agronomique et Vétérinaire Hassan II (IAV Hassan II)",
            "Rabat", "Grande École Publique",
            "Référence continentale en agronomie, agroalimentaire et médecine vétérinaire. L'IAV est le seul établissement marocain " +
            "à former des docteurs vétérinaires d'État et des ingénieurs agronomes, sur un campus de 150 ha avec fermes expérimentales.",
            "https://iav.ac.ma", "contact@iav.ac.ma", "+212 537 77 17 15",
            "Accréditée",
            "Agronomie, Médecine Vétérinaire, Agroalimentaire, Eau et Environnement, Horticulture, Génie Rural, Foresterie",
            "IAV", "#1B5E20",
            List.of(
                concoursPostBac("Ingénierie en Sciences Agronomiques", 120, "Sciences de la Vie et de la Terre", bd(15.00), true),
                concoursPostBac("Médecine Vétérinaire", 80, "Sciences de la Vie et de la Terre", bd(16.00), true),
                concoursPostBac("Ingénierie en Eau et Environnement", 50, "Sciences Mathématiques", bd(14.50), false)
            )
        ),

        new SchoolSeed(
            "École Nationale d'Agriculture de Meknès (ENA Meknès)",
            "Meknès", "Grande École Publique",
            "L'ENA Meknès est spécialisée dans la formation d'ingénieurs agronomes. " +
            "Située au cœur de la région agricole du Saiss, elle entretient des liens directs avec les exploitations de la plaine de Meknès-Fès.",
            "https://www.enameknes.ac.ma", "contact@enameknes.ac.ma", "+212 535 53 11 40",
            "Accréditée",
            "Agronomie Générale, Agro-industrie, Productions Animales, Eau et Environnement",
            "ENA", "#33691E",
            List.of(
                concoursPostBac("Ingénierie Agronomique", 100, "Sciences de la Vie et de la Terre", bd(14.50), true),
                concoursPostBac("Agro-industrie et Qualité Alimentaire", 60, "Sciences de la Vie et de la Terre", bd(14.00), false)
            )
        ),

        new SchoolSeed(
            "École Supérieure des Industries du Textile et de l'Habillement (ESITH)",
            "Casablanca", "Grande École Publique",
            "Seule école d'ingénieurs du secteur textile et habillement au Maroc, l'ESITH répond aux besoins de l'un des piliers de l'export marocain. " +
            "Elle forme des ingénieurs bilingues capables de manager des chaînes de production internationales.",
            "https://www.esith.ac.ma", "contact@esith.ac.ma", "+212 522 23 38 00",
            "Accréditée",
            "Ingénierie Textile et Habillement, Marketing de la Mode, Qualité et Métrologie, Gestion Industrielle",
            "ESITH", "#880E4F",
            List.of(
                concoursPostBac("Ingénierie Textile et Production", 80, "Sciences Mathématiques", bd(13.50), true),
                concoursPostBac("Marketing et Commerce International de la Mode", 50, "Sciences Économiques", bd(13.00), true)
            )
        ),

        new SchoolSeed(
            "École Nationale Supérieure de l'Enseignement Technique de Rabat (ENSET Rabat)",
            "Rabat", "Grande École Publique",
            "L'ENSET Rabat forme des professeurs agrégés de l'enseignement technique secondaire et des cadres en ingénierie pédagogique. " +
            "Elle joue un rôle central dans la Réforme de l'Éducation nationale marocaine.",
            "http://www.enset-rabat.ac.ma", "contact@enset-rabat.ac.ma", "+212 537 77 28 58",
            "Accréditée",
            "Génie Électrique, Génie Informatique, Génie Mécanique, Génie Civil, Sciences de l'Éducation",
            "ENSET", "#4527A0",
            List.of(
                concoursPostBac("Génie Électrique — Formation des Professeurs", 60, "Sciences Mathématiques", bd(14.00), false),
                concoursPostBac("Génie Informatique — Formation des Professeurs", 60, "Sciences Mathématiques", bd(14.20), false)
            )
        ),

        new SchoolSeed(
            "École Nationale d'Architecture de Rabat (ENA)",
            "Rabat", "Grande École Publique",
            "L'ENA est la référence nationale pour la formation des architectes. Elle délivre le diplôme d'Architecte d'État, " +
            "reconnu en Europe et dans le monde arabophone. Fort attachement au patrimoine architectural marocain.",
            "https://www.ena.ac.ma", "contact@ena.ac.ma", "+212 537 68 35 00",
            "Accréditée",
            "Architecture, Urbanisme et Aménagement, Patrimoine Bâti, Design d'Intérieur, Paysagisme",
            "ENA", "#212121",
            List.of(
                concoursPostBac("Architecture — Diplôme d'État (6 ans)", 200, "Sciences Mathématiques / Lettres", bd(14.00), true),
                dossier("Master Architecture et Patrimoine", 2, BigDecimal.ZERO, 30, "Diplôme d'architecte ou équivalent")
            )
        ),

        // ==============================================================
        //  ÉCOLES DE COMMERCE & MANAGEMENT
        // ==============================================================

        new SchoolSeed(
            "École Nationale de Commerce et de Gestion de Casablanca (ENCG Casablanca)",
            "Casablanca", "Grande École Publique",
            "Principal pôle d'excellence en gestion, commerce et management au Maroc. " +
            "L'ENCG Casablanca prépare des managers et entrepreneurs capables de s'insérer immédiatement dans le marché du travail national et international.",
            "https://encg-casa.ma", "contact@encg-casa.ma", "+212 522 23 28 00",
            "Accréditée",
            "Management Général, Commerce International, Finance d'Entreprise, Marketing, Audit et Contrôle de Gestion, RH",
            "ENCG", "#6A1B9A",
            List.of(
                concoursPostBac("Ingénierie du Management — Parcours Finance", 80, "Sciences Économiques", bd(14.50), true),
                concoursPostBac("Ingénierie du Management — Parcours Marketing", 80, "Sciences Économiques", bd(14.20), true),
                concoursPostBac("Commerce International et Logistique", 60, "Sciences Économiques", bd(14.00), false)
            )
        ),

        new SchoolSeed(
            "École Nationale de Commerce et de Gestion de Settat (ENCG Settat)",
            "Settat", "Grande École Publique",
            "Rattachée à l'Université Hassan Premier, l'ENCG Settat offre un accès à une formation en management de haut niveau. " +
            "Elle est particulièrement reconnue pour sa filière Ingénierie d'Affaires et ses partenariats avec des entreprises nationales.",
            "https://encg-settat.ma", "contact@encg-settat.ma", "+212 523 42 10 80",
            "Accréditée",
            "Gestion, Commerce, Finance d'Entreprise, Logistique, Informatique de Gestion",
            "ENCG", "#7B1FA2",
            List.of(
                concoursPostBac("Ingénierie d'Affaires", 80, "Sciences Économiques", bd(14.20), true),
                concoursPostBac("Finance et Comptabilité", 60, "Sciences Économiques", bd(13.80), false)
            )
        ),

        new SchoolSeed(
            "École Nationale de Commerce et de Gestion d'Agadir (ENCG Agadir)",
            "Agadir", "Grande École Publique",
            "Rattachée à l'Université Ibn Zohr, l'ENCG Agadir forme des managers et des entrepreneurs dans le principal hub touristique et agro-industriel du Maroc.",
            "https://encg.uiz.ac.ma", "contact@encg.uiz.ac.ma", "+212 528 24 02 47",
            "Accréditée",
            "Gestion, Commerce, Marketing Touristique, Finance, Agro-Management",
            "ENCG", "#AB47BC",
            List.of(
                concoursPostBac("Management Touristique et Hôtelier", 60, "Sciences Économiques", bd(13.50), true),
                concoursPostBac("Finance et Contrôle de Gestion", 60, "Sciences Économiques", bd(13.80), false)
            )
        ),

        new SchoolSeed(
            "Institut Supérieur de Commerce et d'Administration des Entreprises (ISCAE Casablanca)",
            "Casablanca", "Grande École Publique",
            "L'ISCAE est l'école de management de référence au Maroc depuis 1971. Son MBA et ses Mastères Spécialisés sont très prisés " +
            "par les cadres du secteur privé. Accrédité AACSB, il figure dans les classements africains des meilleures business schools.",
            "https://www.iscae.ac.ma", "contact@iscae.ac.ma", "+212 522 36 53 18",
            "Accréditée AACSB",
            "MBA, Management, Finance, Marketing, RH, Audit, Contrôle de Gestion, Stratégie",
            "ISCAE", "#4A148C",
            List.of(
                dossier("Master of Business Administration (MBA)", 2, bd(35000), 60, "Bac+4 minimum, 2 ans d'expérience professionnelle"),
                dossier("Mastère Spécialisé Finance et Marchés de Capitaux", 1, bd(28000), 40, "Bac+4 en économie, gestion ou ingénierie"),
                dossier("Mastère Spécialisé Marketing et Digital", 1, bd(28000), 40, "Bac+4 en commerce, marketing ou communication")
            )
        ),

        new SchoolSeed(
            "Institut National de Statistique et d'Économie Appliquée (INSEA)",
            "Rabat", "Grande École Publique",
            "L'INSEA forme des ingénieurs statisticiens-économistes et des actuaires qui occupent des postes clés en planification, " +
            "analyse économique, data science et gestion des risques dans les secteurs public (HCP, Bank Al-Maghrib) et privé.",
            "https://www.insea.ac.ma", "contact@insea.ac.ma", "+212 537 77 23 03",
            "Accréditée",
            "Statistiques et Informatique Décisionnelle, Économie Appliquée, Actuariat, Data Science, Démographie",
            "INSEA", "#311B92",
            List.of(
                concoursPostBac("Ingénierie Statistique et Informatique Décisionnelle", 80, "Sciences Mathématiques", bd(15.50), true),
                concoursPostBac("Actuariat et Finance Quantitative", 40, "Sciences Mathématiques", bd(15.80), false)
            )
        ),

        // ==============================================================
        //  UNIVERSITÉS PUBLIQUES
        // ==============================================================

        new SchoolSeed(
            "Université Mohammed V de Rabat (UM5)",
            "Rabat", "Université Publique",
            "La plus ancienne université du Maroc, fondée en 1957 à l'aube de l'Indépendance. " +
            "Elle regroupe facultés, grandes écoles (EMI, ENSIAS) et centres de recherche. Plus de 100 000 étudiants inscrits.",
            "https://www.um5.ac.ma", "info@um5.ac.ma", "+212 537 77 19 02",
            "Accréditée",
            "Droit, Sciences Économiques, Lettres et Sciences Humaines, Sciences, Médecine, Pharmacie, Informatique",
            "UM5", "#0277BD",
            List.of(
                simple("Licence en Informatique", 3, BigDecimal.ZERO, "DOSSIER", "Français/Arabe", 500),
                simple("Licence en Sciences Économiques et Gestion", 3, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 800),
                simple("Master en Droit des Affaires", 2, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 80)
            )
        ),

        new SchoolSeed(
            "Université Hassan II de Casablanca (UH2C)",
            "Casablanca", "Université Publique",
            "Deuxième plus grande université du Maroc avec plus de 170 000 étudiants. Fortement connectée au monde économique casablancais, " +
            "elle regroupe facultés des sciences, droit, lettres et la plupart des grandes écoles de la ville.",
            "https://www.univh2c.ma", "info@univh2c.ma", "+212 522 23 30 00",
            "Accréditée",
            "Sciences, Droit, Lettres, Ingénierie, Médecine, Pharmacie, Dentaire",
            "UH2C", "#01579B",
            List.of(
                simple("Licence en Sciences de la Matière — Physique", 3, BigDecimal.ZERO, "DOSSIER", "Français", 600),
                simple("DEUST Informatique et Réseaux", 2, BigDecimal.ZERO, "DOSSIER", "Français", 200),
                simple("Master Droit Privé — Droit des Contrats", 2, BigDecimal.ZERO, "DOSSIER", "Arabe", 60)
            )
        ),

        new SchoolSeed(
            "Université Cadi Ayyad de Marrakech (UCA)",
            "Marrakech", "Université Publique",
            "Fondée en 1978, l'UCA est l'une des grandes universités marocaines avec 10 facultés et écoles réparties sur 4 campus. " +
            "Elle est reconnue pour ses programmes en tourisme, sciences et ingénierie dans la capitale touristique du Maroc.",
            "https://www.uca.ma", "info@uca.ma", "+212 524 43 47 06",
            "Accréditée",
            "Sciences, Droit, Lettres, Médecine, Ingénierie, Tourisme et Hôtellerie",
            "UCA", "#00695C",
            List.of(
                simple("Licence en Sciences Économiques et Gestion", 3, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 700),
                simple("Licence en Management du Tourisme", 3, BigDecimal.ZERO, "DOSSIER", "Français", 150),
                simple("Master Informatique — Réseaux et Systèmes Distribués", 2, BigDecimal.ZERO, "DOSSIER", "Français", 50)
            )
        ),

        new SchoolSeed(
            "Université Sidi Mohammed Ben Abdellah de Fès (USMBA)",
            "Fès", "Université Publique",
            "L'une des plus grandes universités du Maroc avec plus de 80 000 étudiants. Héritière du prestige académique de Fès, " +
            "cité du savoir, USMBA offre une formation de qualité dans une vaste gamme de disciplines.",
            "https://www.usmba.ac.ma", "info@usmba.ac.ma", "+212 535 65 00 64",
            "Accréditée",
            "Sciences, Droit, Lettres, Médecine, Sciences de l'Ingénieur, Technologies",
            "USMBA", "#006064",
            List.of(
                simple("Licence en Mathématiques et Applications", 3, BigDecimal.ZERO, "DOSSIER", "Français", 400),
                simple("Licence en Droit Public", 3, BigDecimal.ZERO, "DOSSIER", "Arabe", 1000)
            )
        ),

        new SchoolSeed(
            "Université Abdelmalek Essaadi de Tétouan (UAE)",
            "Tétouan", "Université Publique",
            "Principal pôle universitaire du nord du Maroc, très active dans la coopération avec les universités espagnoles. " +
            "Elle dispose de campus à Tétouan, Tanger, Al-Hoceima et Martil.",
            "https://www.uae.ac.ma", "info@uae.ac.ma", "+212 539 97 91 11",
            "Accréditée",
            "Sciences, Droit, Lettres, Informatique, Médecine, Traduction, Sciences de l'Ingénieur",
            "UAE", "#004D40",
            List.of(
                simple("Licence en Traduction — Espagnol/Arabe", 3, BigDecimal.ZERO, "DOSSIER", "Espagnol/Arabe", 100),
                simple("Licence en Informatique", 3, BigDecimal.ZERO, "DOSSIER", "Français", 300)
            )
        ),

        new SchoolSeed(
            "Université Mohammed Premier d'Oujda (UMP)",
            "Oujda", "Université Publique",
            "Pôle académique majeur de la région de l'Oriental, bien connectée aux universités algériennes voisines. " +
            "Elle dispose d'une faculté des sciences pluridisciplinaire très réputée.",
            "https://www.ump.ma", "info@ump.ma", "+212 536 50 06 21",
            "Accréditée",
            "Sciences, Droit, Lettres, Technologies, Médecine, Sciences de l'Ingénieur, Physique",
            "UMP", "#0D47A1",
            List.of(
                simple("Licence en Physique", 3, BigDecimal.ZERO, "DOSSIER", "Français", 300),
                simple("Licence en Sciences Économiques", 3, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 500)
            )
        ),

        new SchoolSeed(
            "Université Ibn Tofail de Kénitra (UIT)",
            "Kénitra", "Université Publique",
            "Fondée en 1989, l'Université Ibn Tofail profite de sa proximité avec Rabat et du tissu industriel de la région. " +
            "Elle est reconnue pour ses programmes de recherche en sciences et ingénierie.",
            "https://www.uit.ac.ma", "info@uit.ac.ma", "+212 537 37 09 12",
            "Accréditée",
            "Sciences, Droit, Lettres, Sciences de l'Ingénieur, Informatique",
            "UIT", "#1565C0",
            List.of(
                simple("Licence en Génie Électrique", 3, BigDecimal.ZERO, "DOSSIER", "Français", 200),
                simple("Licence en Chimie Appliquée", 3, BigDecimal.ZERO, "DOSSIER", "Français", 250)
            )
        ),

        new SchoolSeed(
            "Université Ibn Zohr d'Agadir (UIZ)",
            "Agadir", "Université Publique",
            "Principale université du sud du Maroc, couvrant sept provinces. Nationalement reconnue pour ses programmes en halieutique, " +
            "agro-industrie et tourisme balnéaire.",
            "https://www.uiz.ac.ma", "info@uiz.ac.ma", "+212 528 21 00 07",
            "Accréditée",
            "Sciences, Droit, Lettres, Médecine, Halieutique, Géologie, Tourisme",
            "UIZ", "#2E7D32",
            List.of(
                simple("Licence en Sciences Halieutiques", 3, BigDecimal.ZERO, "DOSSIER", "Français", 120),
                simple("Licence en Économie et Gestion", 3, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 600)
            )
        ),

        new SchoolSeed(
            "Université Moulay Ismail de Meknès (UMI)",
            "Meknès", "Université Publique",
            "Université de la ville impériale, UMI dispose d'une offre de formation complète dans un cadre historique exceptionnel. " +
            "Son campus principal est situé à deux pas des médinas et des remparts almohades.",
            "https://www.umi.ac.ma", "info@umi.ac.ma", "+212 535 46 19 93",
            "Accréditée",
            "Sciences, Droit, Lettres, Sciences de l'Ingénieur, Technologies, Économie",
            "UMI", "#1B5E20",
            List.of(
                simple("Licence en Sciences de la Biologie", 3, BigDecimal.ZERO, "DOSSIER", "Français/Arabe", 400),
                simple("Licence en Génie Civil", 3, BigDecimal.ZERO, "DOSSIER", "Français", 200)
            )
        ),

        new SchoolSeed(
            "Université Chouaib Doukkali d'El Jadida (UCD)",
            "El Jadida", "Université Publique",
            "Université de la côte atlantique, UCD est reconnue pour ses programmes en chimie industrielle. " +
            "Sa proximité avec le complexe chimique de Jorf Lasfar crée des liens directs avec l'industrie phosphatière.",
            "https://www.ucd.ac.ma", "info@ucd.ac.ma", "+212 523 34 44 34",
            "Accréditée",
            "Sciences, Droit, Lettres, Sciences de l'Ingénieur, Chimie",
            "UCD", "#00838F",
            List.of(
                simple("Licence en Chimie Industrielle", 3, BigDecimal.ZERO, "DOSSIER", "Français", 200),
                simple("Licence en Droit des Affaires", 3, BigDecimal.ZERO, "DOSSIER", "Arabe", 400)
            )
        ),

        new SchoolSeed(
            "Université Hassan Premier de Settat (UHP)",
            "Settat", "Université Publique",
            "Université de la région Chaouia, UHP est un pôle académique au service du développement économique régional. " +
            "Elle dispose de la plus grande ENCG du Maroc parmi ses composantes.",
            "https://www.uhp.ac.ma", "info@uhp.ac.ma", "+212 523 72 02 47",
            "Accréditée",
            "Sciences, Droit, Lettres, Gestion, Sciences de l'Ingénieur, Technologies",
            "UHP", "#37474F",
            List.of(
                simple("Licence en Sciences de Gestion", 3, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 500),
                simple("Licence en Informatique", 3, BigDecimal.ZERO, "DOSSIER", "Français", 250)
            )
        ),

        new SchoolSeed(
            "Université Sultan Moulay Slimane de Beni Mellal (USMS)",
            "Beni Mellal", "Université Publique",
            "L'USMS couvre la vaste région Béni Mellal-Khénifra, à cheval entre l'Atlas et la plaine. " +
            "Elle propose des formations diversifiées et participe à la décentralisation de l'enseignement supérieur marocain.",
            "https://www.usms.ac.ma", "info@usms.ac.ma", "+212 523 48 22 53",
            "Accréditée",
            "Sciences, Droit, Lettres, Sciences Économiques, Technologies, Informatique",
            "USMS", "#4E342E",
            List.of(
                simple("Licence en Économie et Gestion", 3, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 400),
                simple("Licence en Physique-Chimie", 3, BigDecimal.ZERO, "DOSSIER", "Français", 300)
            )
        ),

        // ==============================================================
        //  INSTITUTS SPÉCIALISÉS & UNIVERSITÉS PRIVÉES
        // ==============================================================

        new SchoolSeed(
            "Institut Supérieur de l'Information et de la Communication (ISIC)",
            "Rabat", "Institut Spécialisé",
            "L'ISIC est la référence nationale en journalisme, communication et sciences de l'information. " +
            "Ses lauréats dirigent les principales rédactions, agences de communication et services documentaires du Maroc.",
            "https://isic.um5.ac.ma", "contact@isic.um5.ac.ma", "+212 537 77 26 43",
            "Accréditée",
            "Journalisme, Communication Institutionnelle, Sciences de l'Information, Médias Numériques, Relations Publiques",
            "ISIC", "#E65100",
            List.of(
                concoursPostBac("Licence Journalisme et Médias", 60, "Lettres / Sciences", bd(13.50), true),
                concoursPostBac("Licence Communication et Relations Publiques", 60, "Sciences Économiques / Lettres", bd(13.00), true)
            )
        ),

        new SchoolSeed(
            "École Nationale des Beaux-Arts de Tétouan (ENBA)",
            "Tétouan", "École Spécialisée Publique",
            "L'une des plus anciennes écoles d'art du Maroc, fondée en 1945. L'ENBA dispense une formation artistique de haut niveau " +
            "fusionnant les techniques européennes et le patrimoine artistique andalou-marocain unique à Tétouan.",
            "https://www.enba-tetouan.ac.ma", "contact@enba-tetouan.ac.ma", "+212 539 96 49 32",
            "Accréditée",
            "Arts Plastiques, Design Graphique, Céramique d'Art, Peinture, Sculpture, Calligraphie Arabe",
            "ENBA", "#880E4F",
            List.of(
                concoursPostBac("Arts Plastiques et Beaux-Arts", 80, "Lettres et Arts", bd(13.00), true),
                concoursPostBac("Design Graphique et Communication Visuelle", 50, "Lettres et Arts", bd(13.00), false)
            )
        ),

        new SchoolSeed(
            "Université Internationale de Rabat (UIR)",
            "Rabat", "Université Privée",
            "Université privée d'excellence fondée en partenariat avec des universités françaises, l'UIR accueille des étudiants " +
            "de 40 nationalités sur son campus de Rabat-Technopolis. Ses programmes sont double-diplômants avec des partenaires européens.",
            "https://www.uir.ac.ma", "info@uir.ac.ma", "+212 530 10 30 00",
            "Accréditée",
            "Ingénierie, Architecture, Droit, Sciences Politiques, Aéronautique et Aérospatial, Management",
            "UIR", "#C62828",
            List.of(
                dossier("Ingénierie Informatique et Réseaux", 3, bd(45000), 150, "Baccalauréat Sciences Mathématiques avec mention"),
                dossier("Ingénierie Aéronautique", 3, bd(55000), 60, "Baccalauréat Sciences Mathématiques avec mention"),
                dossier("Droit et Sciences Politiques", 3, bd(35000), 80, "Baccalauréat toutes séries")
            )
        ),

        new SchoolSeed(
            "Université Mundiapolis de Casablanca",
            "Casablanca", "Université Privée",
            "Université privée casablancaise offrant des programmes en management, droit, informatique et communication " +
            "dans un cadre international avec des échanges académiques en Europe et en Amérique du Nord.",
            "https://www.mundiapolis.ma", "contact@mundiapolis.ma", "+212 522 36 06 40",
            "Accréditée",
            "Management, Droit des Affaires, Informatique, Communication, Design, Architecture d'Intérieur",
            "MPO", "#AD1457",
            List.of(
                dossier("Bachelor Management et Entrepreneuriat", 3, bd(38000), 100, "Baccalauréat toutes séries, dossier et entretien"),
                dossier("Bachelor Informatique et Big Data", 3, bd(38000), 80, "Baccalauréat Sciences Mathématiques")
            )
        ),

        new SchoolSeed(
            "École Supérieure de Technologie de Casablanca (EST Casablanca)",
            "Casablanca", "École Supérieure de Technologie",
            "L'EST Casablanca propose des formations courtes de type DUT (Bac+2) puis Licence Professionnelle (Bac+3), " +
            "orientées vers l'insertion professionnelle immédiate dans les secteurs technologiques et tertiaires.",
            "https://est.univh2c.ma", "contact@est.univh2c.ma", "+212 522 23 40 00",
            "Accréditée",
            "Techniques de Management, Informatique Décisionnelle, Génie Électrique, Génie Civil, Commerce et Distribution",
            "EST", "#0288D1",
            List.of(
                simple("DUT Informatique et Réseaux", 2, BigDecimal.ZERO, "DOSSIER", "Français", 200),
                simple("DUT Techniques de Management — Finance", 2, BigDecimal.ZERO, "DOSSIER", "Français/Arabe", 180),
                simple("Licence Professionnelle Commerce et Distribution", 1, BigDecimal.ZERO, "DOSSIER", "Français/Arabe", 100)
            )
        ),

        new SchoolSeed(
            "Faculté des Sciences Juridiques, Économiques et Sociales Souissi (FSJES Souissi)",
            "Rabat", "Faculté Publique",
            "Composante emblématique de l'Université Mohammed V, la FSJES Souissi est l'une des facultés de droit les plus réputées du Maroc. " +
            "Elle forme chaque année des milliers de juristes, économistes et experts en sciences sociales.",
            "https://www.um5.ac.ma", "fsjes@um5.ac.ma", "+212 537 77 72 77",
            "Accréditée",
            "Droit Privé, Droit Public, Droit des Affaires, Sciences Économiques, Sciences de Gestion, Sociologie",
            "FSJES", "#283593",
            List.of(
                simple("Licence Fondamentale en Droit Privé", 3, BigDecimal.ZERO, "DOSSIER", "Arabe", 2000),
                simple("Licence Fondamentale en Sciences Économiques", 3, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 1500),
                simple("Master Droit des Affaires Internationales", 2, BigDecimal.ZERO, "DOSSIER", "Arabe/Français", 40)
            )
        ),

        new SchoolSeed(
            "Institut Royal de Formation des Cadres — Jeunesse et Sports (IRFC)",
            "Rabat", "Institut Royal",
            "L'IRFC est l'unique institution marocaine de formation des professeurs d'EPS et des cadres supérieurs du sport et des loisirs. " +
            "Il forme les futurs entraîneurs nationaux et responsables de fédérations sportives.",
            "https://www.irfc.ma", "contact@irfc.ma", "+212 537 78 52 11",
            "Accréditée",
            "Éducation Physique et Sportive, Sciences du Sport et de l'Entraînement, Animation et Loisirs, Management Sportif",
            "IRFC", "#BF360C",
            List.of(
                concoursPostBac("Licence en Éducation Physique et Sportive", 60, "Sciences de la Vie et de la Terre", bd(13.00), true),
                dossier("Master en Management du Sport", 2, BigDecimal.ZERO, 30, "Licence EPS ou équivalent + expérience sportive")
            )
        )
    );

    // ================================================================== //
    //  SCHOOL EXTRAS: slug · headerImageUrl · earthViewUrl · galleryImages //
    //  Key = exact school name as used in ALL_SCHOOLS above.              //
    // ================================================================== //

    private static final java.util.Map<String, String[]> SCHOOL_EXTRAS = buildExtrasMap();

    @SuppressWarnings("all")
    private static java.util.Map<String, String[]> buildExtrasMap() {
        var m = new java.util.HashMap<String, String[]>();
        // helper shorthand  { slug, header, earth, gallery-JSON }
        // shared image banks
        final String eng1 = "https://images.pexels.com/photos/256381/pexels-photo-256381.jpeg";
        final String eng2 = "https://images.pexels.com/photos/3861969/pexels-photo-3861969.jpeg";
        final String eng3 = "https://images.pexels.com/photos/267507/pexels-photo-267507.jpeg";
        final String cs1  = "https://images.pexels.com/photos/1181671/pexels-photo-1181671.jpeg";
        final String cs2  = "https://images.pexels.com/photos/2280571/pexels-photo-2280571.jpeg";
        final String cs3  = "https://images.pexels.com/photos/1181345/pexels-photo-1181345.jpeg";
        final String civ1 = "https://images.pexels.com/photos/2219024/pexels-photo-2219024.jpeg";
        final String civ2 = "https://images.pexels.com/photos/442574/pexels-photo-442574.jpeg";
        final String agr1 = "https://images.pexels.com/photos/440731/pexels-photo-440731.jpeg";
        final String agr2 = "https://images.pexels.com/photos/1382731/pexels-photo-1382731.jpeg";
        final String biz1 = "https://images.pexels.com/photos/1181622/pexels-photo-1181622.jpeg";
        final String biz2 = "https://images.pexels.com/photos/3184306/pexels-photo-3184306.jpeg";
        final String biz3 = "https://images.pexels.com/photos/3184292/pexels-photo-3184292.jpeg";
        final String uni1 = "https://images.pexels.com/photos/207692/pexels-photo-207692.jpeg";
        final String uni2 = "https://images.pexels.com/photos/159775/pexels-photo-159775.jpeg";
        final String uni3 = "https://images.pexels.com/photos/4050315/pexels-photo-4050315.jpeg";
        final String arc1 = "https://images.pexels.com/photos/1134176/pexels-photo-1134176.jpeg";
        final String arc2 = "https://images.pexels.com/photos/1004014/pexels-photo-1004014.jpeg";
        final String art1 = "https://images.pexels.com/photos/1797428/pexels-photo-1797428.jpeg";
        final String spt1 = "https://images.pexels.com/photos/1263426/pexels-photo-1263426.jpeg";
        final String pvt1 = "https://images.pexels.com/photos/274061/pexels-photo-274061.jpeg";
        final String pvt2 = "https://images.pexels.com/photos/1181354/pexels-photo-1181354.jpeg";
        final String min1 = "https://images.pexels.com/photos/1108101/pexels-photo-1108101.jpeg";
        final String for1 = "https://images.pexels.com/photos/167386/pexels-photo-167386.jpeg";
        final String tex1 = "https://images.pexels.com/photos/3768022/pexels-photo-3768022.jpeg";

        // --- Grandes Écoles CNC ---
        m.put("École Mohammadia d'Ingénieurs (EMI)",
            new String[]{"emi", eng1, "https://earth.google.com/web/search/EMI+Rabat+Maroc",
                g(eng2, eng3, cs3)});
        m.put("Institut National des Postes et Télécommunications (INPT)",
            new String[]{"inpt", cs1, "https://earth.google.com/web/search/INPT+Rabat+Maroc",
                g(cs2, cs3, eng1)});
        m.put("École Nationale Supérieure d'Informatique et d'Analyse des Systèmes (ENSIAS)",
            new String[]{"ensias", cs2, "https://earth.google.com/web/search/ENSIAS+Rabat+Maroc",
                g(cs1, cs3, eng3)});
        m.put("École Hassania des Travaux Publics (EHTP)",
            new String[]{"ehtp", civ1, "https://earth.google.com/web/search/EHTP+Casablanca+Maroc",
                g(civ2, eng1, uni1)});
        m.put("École Nationale Supérieure d'Électricité et de Mécanique (ENSEM)",
            new String[]{"ensem", eng2, "https://earth.google.com/web/search/ENSEM+Casablanca+Maroc",
                g(eng3, cs3, eng1)});
        m.put("École Nationale de l'Industrie Minérale (ENIM)",
            new String[]{"enim", min1, "https://earth.google.com/web/search/ENIM+Rabat+Maroc",
                g(eng1, eng2, uni2)});
        m.put("École Nationale Supérieure des Mines de Rabat (ENSMR)",
            new String[]{"ensmr", min1, "https://earth.google.com/web/search/ENSMR+Rabat+Maroc",
                g(eng2, uni2, civ1)});
        m.put("École Nationale Forestière des Ingénieurs (ENFI)",
            new String[]{"enfi", for1, "https://earth.google.com/web/search/ENFI+Sale+Maroc",
                g(agr1, for1, uni3)});
        // --- ENSA ---
        m.put("École Nationale des Sciences Appliquées de Marrakech (ENSA Marrakech)",
            new String[]{"ensa-marrakech", eng3, "https://earth.google.com/web/search/ENSA+Marrakech+Maroc",
                g(eng1, cs3, uni1)});
        m.put("École Nationale des Sciences Appliquées d'Agadir (ENSA Agadir)",
            new String[]{"ensa-agadir", eng1, "https://earth.google.com/web/search/ENSA+Agadir+Maroc",
                g(eng2, cs1, uni3)});
        m.put("École Nationale des Sciences Appliquées de Fès (ENSA Fès)",
            new String[]{"ensa-fes", eng2, "https://earth.google.com/web/search/ENSA+Fes+Maroc",
                g(eng3, cs2, uni2)});
        m.put("École Nationale des Sciences Appliquées de Tanger (ENSA Tanger)",
            new String[]{"ensa-tanger", cs3, "https://earth.google.com/web/search/ENSA+Tanger+Maroc",
                g(eng1, eng3, uni1)});
        m.put("École Nationale des Sciences Appliquées d'Oujda (ENSA Oujda)",
            new String[]{"ensa-oujda", eng3, "https://earth.google.com/web/search/ENSA+Oujda+Maroc",
                g(cs1, eng2, uni3)});
        m.put("École Nationale des Sciences Appliquées de Kénitra (ENSA Kénitra)",
            new String[]{"ensa-kenitra", eng1, "https://earth.google.com/web/search/ENSA+Kenitra+Maroc",
                g(eng2, cs3, uni2)});
        // --- Spécialisées ---
        m.put("Institut Agronomique et Vétérinaire Hassan II (IAV Hassan II)",
            new String[]{"iav", agr1, "https://earth.google.com/web/search/IAV+Hassan+II+Rabat+Maroc",
                g(agr2, for1, uni3)});
        m.put("École Nationale d'Agriculture de Meknès (ENA Meknès)",
            new String[]{"ena-meknes", agr2, "https://earth.google.com/web/search/ENA+Meknes+Maroc",
                g(agr1, for1, uni2)});
        m.put("École Supérieure des Industries du Textile et de l'Habillement (ESITH)",
            new String[]{"esith", tex1, "https://earth.google.com/web/search/ESITH+Casablanca+Maroc",
                g(tex1, biz2, uni3)});
        m.put("École Nationale Supérieure de l'Enseignement Technique de Rabat (ENSET Rabat)",
            new String[]{"enset", cs3, "https://earth.google.com/web/search/ENSET+Rabat+Maroc",
                g(eng1, cs1, uni1)});
        m.put("École Nationale d'Architecture de Rabat (ENA)",
            new String[]{"ena", arc1, "https://earth.google.com/web/search/ENA+Architecture+Rabat+Maroc",
                g(arc2, arc1, uni2)});
        // --- Commerce & Management ---
        m.put("École Nationale de Commerce et de Gestion de Casablanca (ENCG Casablanca)",
            new String[]{"encg-casablanca", biz1, "https://earth.google.com/web/search/ENCG+Casablanca+Maroc",
                g(biz2, biz3, uni1)});
        m.put("École Nationale de Commerce et de Gestion de Settat (ENCG Settat)",
            new String[]{"encg-settat", biz2, "https://earth.google.com/web/search/ENCG+Settat+Maroc",
                g(biz1, biz3, uni3)});
        m.put("École Nationale de Commerce et de Gestion d'Agadir (ENCG Agadir)",
            new String[]{"encg-agadir", biz3, "https://earth.google.com/web/search/ENCG+Agadir+Maroc",
                g(biz1, biz2, uni2)});
        m.put("Institut Supérieur de Commerce et d'Administration des Entreprises (ISCAE Casablanca)",
            new String[]{"iscae", biz1, "https://earth.google.com/web/search/ISCAE+Casablanca+Maroc",
                g(biz3, biz2, pvt1)});
        m.put("Institut National de Statistique et d'Économie Appliquée (INSEA)",
            new String[]{"insea", cs2, "https://earth.google.com/web/search/INSEA+Rabat+Maroc",
                g(cs1, biz1, uni1)});
        // --- Universités Publiques ---
        m.put("Université Mohammed V de Rabat (UM5)",
            new String[]{"um5", uni1, "https://earth.google.com/web/search/Universite+Mohammed+V+Rabat+Maroc",
                g(uni2, uni3, cs3)});
        m.put("Université Hassan II de Casablanca (UH2C)",
            new String[]{"uh2c", uni2, "https://earth.google.com/web/search/Universite+Hassan+II+Casablanca+Maroc",
                g(uni1, uni3, biz1)});
        m.put("Université Cadi Ayyad de Marrakech (UCA)",
            new String[]{"uca", uni3, "https://earth.google.com/web/search/Universite+Cadi+Ayyad+Marrakech+Maroc",
                g(uni1, uni2, agr1)});
        m.put("Université Sidi Mohammed Ben Abdellah de Fès (USMBA)",
            new String[]{"usmba", uni1, "https://earth.google.com/web/search/Universite+USMBA+Fes+Maroc",
                g(uni3, uni2, cs1)});
        m.put("Université Abdelmalek Essaadi de Tétouan (UAE)",
            new String[]{"uae", uni2, "https://earth.google.com/web/search/Universite+Abdelmalek+Essaadi+Tetouan+Maroc",
                g(uni1, uni3, biz2)});
        m.put("Université Mohammed Premier d'Oujda (UMP)",
            new String[]{"ump", uni3, "https://earth.google.com/web/search/Universite+Mohammed+Premier+Oujda+Maroc",
                g(uni2, uni1, agr2)});
        m.put("Université Ibn Tofail de Kénitra (UIT)",
            new String[]{"uit", uni1, "https://earth.google.com/web/search/Universite+Ibn+Tofail+Kenitra+Maroc",
                g(uni3, eng1, cs2)});
        m.put("Université Ibn Zohr d'Agadir (UIZ)",
            new String[]{"uiz", uni2, "https://earth.google.com/web/search/Universite+Ibn+Zohr+Agadir+Maroc",
                g(uni1, agr1, uni3)});
        m.put("Université Moulay Ismail de Meknès (UMI)",
            new String[]{"umi", uni3, "https://earth.google.com/web/search/Universite+Moulay+Ismail+Meknes+Maroc",
                g(uni1, uni2, agr2)});
        m.put("Université Chouaib Doukkali d'El Jadida (UCD)",
            new String[]{"ucd", uni1, "https://earth.google.com/web/search/Universite+Chouaib+Doukkali+El+Jadida+Maroc",
                g(uni2, cs2, uni3)});
        m.put("Université Hassan Premier de Settat (UHP)",
            new String[]{"uhp", uni2, "https://earth.google.com/web/search/Universite+Hassan+Premier+Settat+Maroc",
                g(uni3, biz1, uni1)});
        m.put("Université Sultan Moulay Slimane de Beni Mellal (USMS)",
            new String[]{"usms", uni3, "https://earth.google.com/web/search/Universite+Sultan+Moulay+Slimane+Beni+Mellal",
                g(uni1, uni2, agr1)});
        // --- Instituts & Privées ---
        m.put("Institut Supérieur de l'Information et de la Communication (ISIC)",
            new String[]{"isic", cs3, "https://earth.google.com/web/search/ISIC+Rabat+Maroc",
                g(cs1, biz2, uni1)});
        m.put("École Nationale des Beaux-Arts de Tétouan (ENBA)",
            new String[]{"enba", art1, "https://earth.google.com/web/search/ENBA+Tetouan+Maroc",
                g(arc1, art1, uni2)});
        m.put("Université Internationale de Rabat (UIR)",
            new String[]{"uir", pvt1, "https://earth.google.com/web/search/Universite+Internationale+Rabat+Maroc",
                g(pvt2, cs1, biz1)});
        m.put("Université Mundiapolis de Casablanca",
            new String[]{"mundiapolis", pvt2, "https://earth.google.com/web/search/Mundiapolis+Casablanca+Maroc",
                g(pvt1, biz1, cs3)});
        m.put("École Supérieure de Technologie de Casablanca (EST Casablanca)",
            new String[]{"est-casablanca", eng3, "https://earth.google.com/web/search/EST+Casablanca+Maroc",
                g(eng1, cs3, uni3)});
        m.put("Faculté des Sciences Juridiques, Économiques et Sociales Souissi (FSJES Souissi)",
            new String[]{"fsjes-souissi", uni2, "https://earth.google.com/web/search/FSJES+Souissi+Rabat+Maroc",
                g(uni1, biz2, uni3)});
        m.put("Institut Royal de Formation des Cadres \u2014 Jeunesse et Sports (IRFC)",
            new String[]{"irfc", spt1, "https://earth.google.com/web/search/IRFC+Rabat+Maroc",
                g(spt1, agr1, uni3)});
        return java.util.Collections.unmodifiableMap(m);
    }

    /** Build a JSON gallery array from 3 image URLs. */
    private static String g(String a, String b, String c) {
        return "[\"" + a + "\",\"" + b + "\",\"" + c + "\"]";
    }

}
