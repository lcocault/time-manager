package fr.tools.riskmanager.interfaces.rest;

import fr.tools.riskmanager.application.service.RiskService;
import fr.tools.riskmanager.interfaces.dto.CreateRiskRequest;
import fr.tools.riskmanager.interfaces.dto.RiskResponse;
import fr.tools.riskmanager.interfaces.dto.UpdateRiskRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risks")
public class RiskController {
    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RiskResponse createRisk(@Valid @RequestBody CreateRiskRequest request) {
        return RiskResponse.from(riskService.createRisk(request.toCommand()));
    }

    @GetMapping
    public List<RiskResponse> listRisks() {
        return riskService.listRisks().stream().map(RiskResponse::from).toList();
    }

    @PatchMapping("/{riskId}")
    public RiskResponse updateRisk(@PathVariable Long riskId, @Valid @RequestBody UpdateRiskRequest request) {
        return RiskResponse.from(riskService.updateRisk(riskId, request.toCommand()));
    }

    @DeleteMapping("/{riskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRisk(@PathVariable Long riskId) {
        riskService.deleteRisk(riskId);
    }
}