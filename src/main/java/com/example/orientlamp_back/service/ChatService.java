package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.ChatRequestDTO;
import com.example.orientlamp_back.dto.ChatResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${groq.api.key:}")
    private String groqApiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=";

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String GROQ_MODEL = "llama-3.3-70b-versatile";

    private static final String SYSTEM_PROMPT =
            "Tu es OrientIA, un conseiller d'orientation universitaire expert du système d'enseignement supérieur marocain. " +
            "Tu travailles pour OrientLamp, une plateforme d'aide à l'orientation au Maroc. " +
            "Tu parles exclusivement en français (sauf si l'utilisateur écrit en arabe, alors réponds en arabe). " +
            "\n\n⚠️ RÈGLE ABSOLUE — HORS-SUJET : Si la question posée n'est pas liée à l'orientation scolaire, universitaire ou professionnelle au Maroc (choix de filière, concours, établissements, débouchés, bourses, études à l'étranger), " +
            "tu DOIS refuser poliment en disant exactement : \"Je suis spécialisé uniquement dans l'orientation universitaire au Maroc. Je ne peux pas répondre à cette question, mais je serais ravi de vous aider à choisir votre filière, comprendre un concours ou découvrir des établissements adaptés à votre profil.\". " +
            "N'invente JAMAIS de réponse à des sujets hors orientation. Peu importe comment la question est formulée." +
            "\n\nTu as une connaissance approfondie de :\n" +
            "- Le système éducatif marocain : Baccalauréat (toutes filières), CPGE, BTS, DUT/DTS, Licence, Master, Doctorat\n" +
            "- Les grandes écoles d'ingénieurs : ENSA (Agadir, Casablanca, Fès, Kénitra, Marrakech, Oujda, Rabat, Safi, Tétouan), " +
              "EMI (École Mohammadia d'Ingénieurs), ENSIAS, INPT, EHTP (École Hassania des Travaux Publics), " +
              "INSEA, ENSMR, ESITH, ENSA Al Hoceima\n" +
            "- Les écoles de commerce et management : ENCG (15 campus), ISCAE, HEM, ISGA, Université Internationale de Rabat\n" +
            "- Les facultés : Faculté des Sciences, Faculté des Sciences et Techniques (FST), Faculté des Sciences Juridiques Économiques et Sociales, Faculté de Médecine et Pharmacie, Faculté de Droit\n" +
            "- Les instituts technologiques : EST (École Supérieure de Technologie), OFPPT (filières BTS/TS)\n" +
            "- Les concours nationaux : CNC (Concours National Commun) filières MP/PSI/TSI/BCPST/ECS/ECT, " +
              "CNPQ (Classes Prépas Quota), Concours médecine/pharmacie/dentaire, Concours ISCAE, Concours ENCG, Concours enseignement\n" +
            "- Les critères d'admission : notes du bac, filière, moyennes trimestrielles en prépa, classements CNC\n" +
            "- Les débouchés professionnels et secteurs porteurs au Maroc (IT, industrie, finance, santé, énergie, tourisme…)\n" +
            "- Les bourses d'études (nationales et à l'étranger : France, Canada, Espagne, Allemagne, Chine)\n" +
            "- Les villes universitaires marocaines et coût de la vie\n" +
            "\nRègles de comportement :\n" +
            "- Sois chaleureux, encourageant et pédagogique\n" +
            "- Donne des réponses concrètes et pratiques avec des exemples réels marocains\n" +
            "- Si tu ne connais pas une information spécifique, dis-le honnêtement et oriente vers des sources officielles\n" +
            "- Ne fabricule jamais de données (notes minimales, classements) que tu ne connais pas avec certitude\n" +
            "- Quand un étudiant partage son profil (filière bac, notes), aide-le à identifier les établissements accessibles\n" +
            "- Limite tes réponses à 3-5 paragraphes courts pour rester lisible sur mobile\n" +
            "- Utilise des listes à puces quand tu énumères des options";

    /** Build the effective system prompt, injecting user profile when available */
    private String buildSystemPrompt(String userProfile) {
        if (userProfile == null || userProfile.isBlank()) return SYSTEM_PROMPT;
        return SYSTEM_PROMPT +
               "\n\n📋 PROFIL DE L'UTILISATEUR (utilise ces informations pour personnaliser tes conseils) :\n" +
               userProfile;
    }

    // ------------------------------------------------------------------ //
    //  Public entry point — dispatches to Groq or Gemini                  //
    // ------------------------------------------------------------------ //
    public ChatResponseDTO chat(ChatRequestDTO request) {
        String provider = Optional.ofNullable(request.getProvider())
                .map(String::toLowerCase).orElse("groq");
        try {
            return "gemini".equals(provider) ? callGemini(request) : callGroq(request);
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();
            log.error("{} API error {}: {}", provider, ex.getStatusCode(), body);
            if (ex.getStatusCode().value() == 429)
                return new ChatResponseDTO(buildQuotaMessage(provider, body));
            if (ex.getStatusCode().value() == 401 || ex.getStatusCode().value() == 403)
                return new ChatResponseDTO("⚠️ Clé API " + provider + " invalide ou non autorisée.");
            return new ChatResponseDTO("Erreur API " + provider + " (" + ex.getStatusCode().value() + "). Veuillez réessayer.");
        } catch (Exception ex) {
            log.error("{} API call failed: {}", provider, ex.getMessage());
            return new ChatResponseDTO("Je rencontre un problème technique temporaire. Veuillez réessayer dans un instant.");
        }
    }

    // ------------------------------------------------------------------ //
    //  Groq  (OpenAI-compatible format)                                   //
    // ------------------------------------------------------------------ //
    private ChatResponseDTO callGroq(ChatRequestDTO request) throws Exception {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", GROQ_MODEL);
        body.put("max_tokens", 1024);
        body.put("temperature", 0.75);

        ArrayNode messages = objectMapper.createArrayNode();

        // System message
        ObjectNode sys = objectMapper.createObjectNode();
        sys.put("role", "system");
        sys.put("content", buildSystemPrompt(request.getUserProfile()));
        messages.add(sys);

        // History
        if (request.getHistory() != null) {
            for (ChatRequestDTO.HistoryItem item : request.getHistory()) {
                ObjectNode msg = objectMapper.createObjectNode();
                // Groq uses "assistant" instead of "model"
                msg.put("role", "model".equals(item.getRole()) ? "assistant" : "user");
                msg.put("content", item.getText());
                messages.add(msg);
            }
        }

        // Latest user message
        ObjectNode userMsg = objectMapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", request.getMessage());
        messages.add(userMsg);

        body.set("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(GROQ_URL, entity, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        String reply = root.path("choices").get(0)
                .path("message").path("content")
                .asText("Je suis désolé, je n'ai pas pu générer une réponse.");
        return new ChatResponseDTO(reply);
    }

    // ------------------------------------------------------------------ //
    //  Gemini                                                              //
    // ------------------------------------------------------------------ //
    private ChatResponseDTO callGemini(ChatRequestDTO request) throws Exception {
        try {
            return callGeminiInternal(request, true);
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 429) {
                log.warn("Gemini search quota hit (429), retrying without grounding...");
                return callGeminiInternal(request, false);
            }
            throw ex;
        }
    }

    private ChatResponseDTO callGeminiInternal(ChatRequestDTO request, boolean withSearch) throws Exception {
        ObjectNode body = objectMapper.createObjectNode();

        // System instruction
        ObjectNode sysInstruction = objectMapper.createObjectNode();
        ArrayNode sysParts = objectMapper.createArrayNode();
        ObjectNode sysText = objectMapper.createObjectNode();
        sysText.put("text", buildSystemPrompt(request.getUserProfile()));
        sysParts.add(sysText);
        sysInstruction.set("parts", sysParts);
        body.set("system_instruction", sysInstruction);

        // History + user message
        ArrayNode contents = objectMapper.createArrayNode();
        if (request.getHistory() != null) {
            for (ChatRequestDTO.HistoryItem item : request.getHistory()) {
                ObjectNode turn = objectMapper.createObjectNode();
                turn.put("role", item.getRole());
                ArrayNode parts = objectMapper.createArrayNode();
                ObjectNode part = objectMapper.createObjectNode();
                part.put("text", item.getText());
                parts.add(part);
                turn.set("parts", parts);
                contents.add(turn);
            }
        }
        ObjectNode userTurn = objectMapper.createObjectNode();
        userTurn.put("role", "user");
        ArrayNode userParts = objectMapper.createArrayNode();
        ObjectNode userPart = objectMapper.createObjectNode();
        userPart.put("text", request.getMessage());
        userParts.add(userPart);
        userTurn.set("parts", userParts);
        contents.add(userTurn);
        body.set("contents", contents);

        // Google Search grounding — enabled by default, disabled on quota fallback
        if (withSearch) {
            ArrayNode tools = objectMapper.createArrayNode();
            ObjectNode searchTool = objectMapper.createObjectNode();
            searchTool.set("google_search", objectMapper.createObjectNode());
            tools.add(searchTool);
            body.set("tools", tools);
        }

        // Generation config
        ObjectNode genConfig = objectMapper.createObjectNode();
        genConfig.put("maxOutputTokens", 1024);
        genConfig.put("temperature", 0.75);
        genConfig.put("topP", 0.95);
        body.set("generationConfig", genConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_URL + geminiApiKey, entity, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        String reply = root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText("Je suis désolé, je n'ai pas pu générer une réponse.");
        return new ChatResponseDTO(reply);
    }

    // ------------------------------------------------------------------ //
    //  Quota error message builder                                         //
    // ------------------------------------------------------------------ //
    private String buildQuotaMessage(String provider, String errorBody) {
        // Per-minute rate limit mentions "per_minute", "RPM", or "requests per minute"
        // Everything else (including "exceeded your current quota") is treated as daily quota
        boolean isPerMinute = errorBody != null &&
                (errorBody.contains("per_minute") || errorBody.contains("RPM") ||
                 errorBody.contains("per minute") || errorBody.contains("requests per minute"));

        if ("gemini".equals(provider)) {
            if (isPerMinute) {
                return "⏳ Gemini a atteint sa limite de **15 requêtes/minute**.\n" +
                       "Attendez **~60 secondes** puis réessayez, ou passez sur **Groq** en haut.";
            } else {
                // Daily quota — calculate exact time until midnight Morocco time
                java.time.ZonedDateTime now = java.time.ZonedDateTime.now(java.time.ZoneId.of("Africa/Casablanca"));
                java.time.ZonedDateTime midnight = now.toLocalDate().plusDays(1)
                        .atStartOfDay(java.time.ZoneId.of("Africa/Casablanca"));
                long minutes = java.time.Duration.between(now, midnight).toMinutes();
                long hours   = minutes / 60;
                long mins    = minutes % 60;
                String wait  = hours > 0 ? hours + "h" + (mins > 0 ? mins + "min" : "") : mins + " minutes";
                return "⏳ Le quota journalier de Gemini est atteint.\n" +
                       "Il se renouvelle dans **" + wait + "** (à minuit, heure du Maroc).\n\n" +
                       "💡 En attendant, passez sur **Groq** (Llama 3.3) avec le sélecteur en haut — il reste disponible.";
            }
        } else {
            // Groq
            return "⏳ Le quota Groq est temporairement atteint.\n" +
                   "Il se renouvelle automatiquement dans quelques secondes — réessayez dans **1 minute**.\n\n" +
                   "💡 Vous pouvez aussi basculer sur **Gemini** avec le sélecteur en haut.";
        }
    }
}
