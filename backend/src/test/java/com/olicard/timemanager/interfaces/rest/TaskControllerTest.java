package com.olicard.timemanager.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.olicard.timemanager.application.dto.TaskDTO;
import com.olicard.timemanager.application.service.TaskService;
import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    @Mock
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void shouldCreateTaskThroughRestEndpoint() throws Exception {
        when(taskService.createTask(any())).thenReturn(new TaskDTO(
                5L,
                "Préparer sprint",
                "",
                LocalDateTime.of(2026, 6, 10, 9, 0),
                90,
                Importance.HIGH,
                Urgency.HIGH,
                TaskType.WORK,
                false,
                9
        ));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateTaskPayload())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.priorityScore").value(9));
    }

    private record CreateTaskPayload(
            String title,
            String description,
            LocalDateTime deadline,
            int estimatedMinutes,
            Importance importance,
            Urgency urgency,
            TaskType type
    ) {
        private CreateTaskPayload() {
            this("Préparer sprint", "", LocalDateTime.of(2026, 6, 10, 9, 0), 90, Importance.HIGH, Urgency.HIGH, TaskType.WORK);
        }
    }
}
