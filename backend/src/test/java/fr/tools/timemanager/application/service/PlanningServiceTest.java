package fr.tools.timemanager.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import fr.tools.timemanager.domain.model.Importance;
import fr.tools.timemanager.domain.model.Task;
import fr.tools.timemanager.domain.model.TaskType;
import fr.tools.timemanager.domain.model.TimeSlot;
import fr.tools.timemanager.domain.model.Urgency;
import fr.tools.timemanager.domain.port.CalendarSyncPort;
import fr.tools.timemanager.domain.port.TaskRepositoryPort;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanningServiceTest {
    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private CalendarSyncPort calendarSyncPort;

    @InjectMocks
    private PlanningService planningService;

    @Test
    void shouldAssignHighestPriorityTaskToMatchingSlotType() {
        Task workTask = new Task(1L, "Préparer atelier", "", LocalDateTime.of(2026, 6, 9, 10, 0), Duration.ofMinutes(120), Importance.HIGH, Urgency.HIGH, TaskType.WORK, false);
        Task personalTask = new Task(2L, "Sport", "", null, Duration.ofMinutes(60), Importance.MEDIUM, Urgency.MEDIUM, TaskType.PERSONAL, false);
        List<TimeSlot> slots = List.of(
                new TimeSlot(LocalDateTime.of(2026, 6, 9, 9, 0), LocalDateTime.of(2026, 6, 9, 12, 0), TaskType.WORK),
                new TimeSlot(LocalDateTime.of(2026, 6, 9, 18, 0), LocalDateTime.of(2026, 6, 9, 20, 0), TaskType.PERSONAL)
        );
        when(taskRepositoryPort.findAll()).thenReturn(List.of(personalTask, workTask));
        when(calendarSyncPort.getAvailableSlots(any(LocalDate.class), eq(5))).thenReturn(slots);

        var planning = planningService.getFiveDayPlanning();

        assertEquals(2, planning.slots().size());
        assertEquals("Préparer atelier", planning.slots().get(0).taskTitle());
        assertEquals("Sport", planning.slots().get(1).taskTitle());
    }
}
