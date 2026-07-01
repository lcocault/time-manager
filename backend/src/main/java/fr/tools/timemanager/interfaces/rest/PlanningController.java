package fr.tools.timemanager.interfaces.rest;

import fr.tools.timemanager.application.service.PlanningService;
import fr.tools.timemanager.interfaces.dto.PlanningResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planning")
public class PlanningController {
    private final PlanningService planningService;

    public PlanningController(PlanningService planningService) {
        this.planningService = planningService;
    }

    @GetMapping("/5days")
    public PlanningResponse getFiveDayPlanning() {
        return PlanningResponse.from(planningService.getFiveDayPlanning());
    }
}
