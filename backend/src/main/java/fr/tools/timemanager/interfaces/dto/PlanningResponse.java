package fr.tools.timemanager.interfaces.dto;

import fr.tools.timemanager.application.dto.PlanningDTO;
import fr.tools.timemanager.application.dto.PlanningSlotDTO;
import fr.tools.timemanager.domain.model.TaskType;
import java.time.LocalDateTime;
import java.util.List;

public record PlanningResponse(List<PlanningSlotResponse> slots) {
    public static PlanningResponse from(PlanningDTO planning) {
        return new PlanningResponse(planning.slots().stream().map(PlanningSlotResponse::from).toList());
    }

    public record PlanningSlotResponse(
            LocalDateTime start,
            LocalDateTime end,
            TaskType type,
            Long taskId,
            String taskTitle,
            Long allocatedMinutes
    ) {
        public static PlanningSlotResponse from(PlanningSlotDTO slot) {
            return new PlanningSlotResponse(slot.start(), slot.end(), slot.type(), slot.taskId(), slot.taskTitle(), slot.allocatedMinutes());
        }
    }
}
