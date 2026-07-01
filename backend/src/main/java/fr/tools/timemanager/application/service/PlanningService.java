package fr.tools.timemanager.application.service;

import fr.tools.timemanager.application.dto.PlanningDTO;
import fr.tools.timemanager.application.dto.PlanningSlotDTO;
import fr.tools.timemanager.domain.model.Task;
import fr.tools.timemanager.domain.model.TimeSlot;
import fr.tools.timemanager.domain.port.CalendarSyncPort;
import fr.tools.timemanager.domain.port.TaskRepositoryPort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class PlanningService {
    private final TaskRepositoryPort taskRepositoryPort;
    private final CalendarSyncPort calendarSyncPort;

    public PlanningService(TaskRepositoryPort taskRepositoryPort, CalendarSyncPort calendarSyncPort) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.calendarSyncPort = calendarSyncPort;
    }

    public PlanningDTO getFiveDayPlanning() {
        List<TimeSlot> availableSlots = calendarSyncPort.getAvailableSlots(LocalDate.now(), 5);
        Map<Long, RemainingTask> remainingTasks = new LinkedHashMap<>();

        taskRepositoryPort.findAll().stream()
                .filter(task -> !task.completed())
                .sorted(Comparator.comparingInt(Task::priorityScore)
                        .reversed()
                        .thenComparing(task -> task.deadline() == null ? LocalDateTime.MAX : task.deadline()))
                .forEach(task -> remainingTasks.put(task.id(), new RemainingTask(task, task.estimatedTime().toMinutes())));

        List<PlanningSlotDTO> plannedSlots = new ArrayList<>();
        for (TimeSlot slot : availableSlots) {
            RemainingTask selectedTask = remainingTasks.values().stream()
                    .filter(task -> task.remainingMinutes > 0 && task.task.type() == slot.type())
                    .findFirst()
                    .orElse(null);

            if (selectedTask == null) {
                plannedSlots.add(new PlanningSlotDTO(slot.start(), slot.end(), slot.type(), null, null, null));
                continue;
            }

            long allocatedMinutes = Math.min(selectedTask.remainingMinutes, slot.durationMinutes());
            selectedTask.remainingMinutes -= allocatedMinutes;
            plannedSlots.add(new PlanningSlotDTO(
                    slot.start(),
                    slot.end(),
                    slot.type(),
                    selectedTask.task.id(),
                    selectedTask.task.title(),
                    allocatedMinutes
            ));
        }

        return new PlanningDTO(plannedSlots);
    }

    private static final class RemainingTask {
        private final Task task;
        private long remainingMinutes;

        private RemainingTask(Task task, long remainingMinutes) {
            this.task = task;
            this.remainingMinutes = remainingMinutes;
        }
    }
}
