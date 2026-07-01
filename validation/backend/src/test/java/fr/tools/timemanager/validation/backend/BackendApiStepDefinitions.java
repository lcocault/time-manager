package fr.tools.timemanager.validation.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;

public class BackendApiStepDefinitions {
    private static final String BACKEND_BASE_URL =
            System.getenv().getOrDefault("BACKEND_BASE_URL", "http://127.0.0.1:8080");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private int lastStatus;
    private String lastBody;
    private Long lastCreatedRiskId;
    private Long lastDeletedRiskId;

    @Given("le backend API est initialise")
    public void initializeBackendApi() throws Exception {
        HttpResponse<String> healthCheck = send("GET", "/api/tasks", null);
        Assertions.assertEquals(
                200,
                healthCheck.statusCode(),
                "Backend indisponible. Lance le backend avant ces tests, ex: cd backend && mvn spring-boot:run"
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "Seed " + UUID.randomUUID());
        payload.put("description", "fixture for backend validation");
        payload.put("estimatedMinutes", 15);
        payload.put("importance", "LOW");
        payload.put("urgency", "LOW");
        payload.put("type", "WORK");

        HttpResponse<String> create = send("POST", "/api/tasks", objectMapper.writeValueAsString(payload));
        Assertions.assertEquals(201, create.statusCode(), "Impossible de creer la fixture backend");
    }

    @When("^je fais une requete GET sur /api/tasks$")
    public void getTasks() throws Exception {
        HttpResponse<String> response = send("GET", "/api/tasks", null);
        lastStatus = response.statusCode();
        lastBody = response.body();
    }

    @When("^je fais une requete GET sur /api/planning/5days$")
    public void getPlanning() throws Exception {
        HttpResponse<String> response = send("GET", "/api/planning/5days", null);
        lastStatus = response.statusCode();
        lastBody = response.body();
    }

    @When("^je fais une requete GET sur /api/risks$")
    public void getRisks() throws Exception {
        HttpResponse<String> response = send("GET", "/api/risks", null);
        lastStatus = response.statusCode();
        lastBody = response.body();
    }

    @When("je cree une tache avec le titre {string} et {int} minutes")
    public void createTask(String title, Integer minutes) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("description", "");
        payload.put("deadline", null);
        payload.put("estimatedMinutes", minutes);
        payload.put("importance", "MEDIUM");
        payload.put("urgency", "MEDIUM");
        payload.put("type", "WORK");

        HttpResponse<String> response = send("POST", "/api/tasks", objectMapper.writeValueAsString(payload));
        lastStatus = response.statusCode();
        lastBody = response.body();
    }

    @When("je cree un risque avec le facteur {string}")
    public void createRisk(String riskFactor) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("riskFactor", riskFactor);
        payload.put("consequences", "Impact planning et charge equipe");
        payload.put("impactNature", "Planning");
        payload.put("measurementMode", "SCALE");
        payload.put("initialImpactScale", 4);
        payload.put("initialImpactEuros", null);
        payload.put("initialProbabilityScale", 3);
        payload.put("initialProbabilityValue", null);
        payload.put("actionId", 1);
        payload.put("action", "Mettre en place un plan de mitigation");
        payload.put("actionNature", "MITIGATE");
        payload.put("owner", "BOTH");
        payload.put("mitigatedImpactScale", 2);
        payload.put("mitigatedImpactEuros", null);
        payload.put("correctedProbabilityScale", 2);
        payload.put("correctedProbabilityValue", null);

        HttpResponse<String> response = send("POST", "/api/risks", objectMapper.writeValueAsString(payload));
        lastStatus = response.statusCode();
        lastBody = response.body();

        if (lastStatus == 201) {
            Map<String, Object> created = objectMapper.readValue(lastBody, new TypeReference<>() {
            });
            Number id = (Number) created.get("id");
            if (id != null) {
                lastCreatedRiskId = id.longValue();
            }
        }
    }

    @When("je modifie le dernier risque cree avec le facteur {string}")
    public void updateLastCreatedRisk(String newRiskFactor) throws Exception {
        Assertions.assertNotNull(lastCreatedRiskId, "Aucun risque cree precedemment pour le PATCH");

        Map<String, Object> payload = new HashMap<>();
        payload.put("riskFactor", newRiskFactor);

        HttpResponse<String> response = send(
                "PATCH",
                "/api/risks/" + lastCreatedRiskId,
                objectMapper.writeValueAsString(payload)
        );
        lastStatus = response.statusCode();
        lastBody = response.body();
    }

    @When("je supprime le dernier risque cree")
    public void deleteLastCreatedRisk() throws Exception {
        Assertions.assertNotNull(lastCreatedRiskId, "Aucun risque cree precedemment pour le DELETE");
        lastDeletedRiskId = lastCreatedRiskId;

        HttpResponse<String> response = send("DELETE", "/api/risks/" + lastCreatedRiskId, null);
        lastStatus = response.statusCode();
        lastBody = response.body();
    }

    @Then("le statut HTTP est {int}")
    public void checkHttpStatus(Integer status) {
        Assertions.assertEquals(status, lastStatus);
    }

    @Then("la reponse contient au moins {int} taches")
    public void checkTasksResponseSize(Integer minimum) throws Exception {
        List<Map<String, Object>> tasks = objectMapper.readValue(lastBody, new TypeReference<>() {
        });
        Assertions.assertTrue(tasks.size() >= minimum);
    }

    @Then("la reponse contient au moins {int} risques")
    public void checkRisksResponseSize(Integer minimum) throws Exception {
        List<Map<String, Object>> risks = objectMapper.readValue(lastBody, new TypeReference<>() {
        });
        Assertions.assertTrue(risks.size() >= minimum);
    }

    @Then("la reponse planning contient au moins {int} creneaux")
    public void checkPlanningResponseSize(Integer minimum) throws Exception {
        Map<String, Object> response = objectMapper.readValue(lastBody, new TypeReference<>() {
        });
        List<?> slots = (List<?>) response.get("slots");
        Assertions.assertNotNull(slots);
        Assertions.assertTrue(slots.size() >= minimum);
    }

    @Then("la reponse de creation contient le titre {string}")
    public void checkCreatedTaskTitle(String title) throws Exception {
        Map<String, Object> response = objectMapper.readValue(lastBody, new TypeReference<>() {
        });
        Assertions.assertEquals(title, response.get("title"));
    }

    @Then("la reponse de creation risque contient le facteur {string}")
    public void checkCreatedRiskFactor(String riskFactor) throws Exception {
        Map<String, Object> response = objectMapper.readValue(lastBody, new TypeReference<>() {
        });
        Assertions.assertEquals(riskFactor, response.get("riskFactor"));
    }

    @Then("la reponse de mise a jour risque contient le facteur {string}")
    public void checkUpdatedRiskFactor(String riskFactor) throws Exception {
        Map<String, Object> response = objectMapper.readValue(lastBody, new TypeReference<>() {
        });
        Assertions.assertEquals(riskFactor, response.get("riskFactor"));
    }

    @Then("la liste des risques ne contient pas le dernier risque supprime")
    public void checkDeletedRiskMissingInList() throws Exception {
        Assertions.assertNotNull(lastDeletedRiskId, "Aucun risque supprime a verifier");
        List<Map<String, Object>> risks = objectMapper.readValue(lastBody, new TypeReference<>() {
        });
        boolean stillExists = risks.stream().anyMatch(risk -> {
            Number id = (Number) risk.get("id");
            return id != null && id.longValue() == lastDeletedRiskId;
        });
        Assertions.assertFalse(stillExists, "Le risque supprime est encore present dans la liste");
    }

    private HttpResponse<String> send(String method, String path, String body) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json");

        if ("GET".equals(method)) {
            requestBuilder.GET();
        } else if ("POST".equals(method)) {
            requestBuilder.header("Content-Type", "application/json");
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        } else if ("PATCH".equals(method)) {
            requestBuilder.header("Content-Type", "application/json");
            requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        } else if ("DELETE".equals(method)) {
            requestBuilder.DELETE();
        } else {
            throw new IllegalArgumentException("Methode HTTP non supportee: " + method);
        }

        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
