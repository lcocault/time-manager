package com.olicard.timemanager.validation.backend;

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

    private HttpResponse<String> send(String method, String path, String body) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json");

        if ("POST".equals(method)) {
            requestBuilder.header("Content-Type", "application/json");
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        } else {
            requestBuilder.GET();
        }

        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
