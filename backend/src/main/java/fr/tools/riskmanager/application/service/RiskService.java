package fr.tools.riskmanager.application.service;

import fr.tools.riskmanager.application.dto.CreateRiskCommand;
import fr.tools.riskmanager.application.dto.RiskDTO;
import fr.tools.riskmanager.application.dto.UpdateRiskCommand;
import fr.tools.riskmanager.domain.exception.RiskNotFoundException;
import fr.tools.riskmanager.domain.model.Risk;
import fr.tools.riskmanager.domain.port.RiskRepositoryPort;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RiskService {
    private final RiskRepositoryPort riskRepositoryPort;

    public RiskService(RiskRepositoryPort riskRepositoryPort) {
        this.riskRepositoryPort = riskRepositoryPort;
    }

    public RiskDTO createRisk(CreateRiskCommand command) {
        Risk risk = toDomain(null, command);
        return toDto(riskRepositoryPort.save(risk));
    }

    public List<RiskDTO> listRisks() {
        return riskRepositoryPort.findAll().stream()
                .sorted(Comparator.comparingLong(Risk::id).reversed())
                .map(this::toDto)
                .toList();
    }

    public RiskDTO updateRisk(Long riskId, UpdateRiskCommand command) {
        Risk existing = riskRepositoryPort.findById(riskId)
                .orElseThrow(() -> new RiskNotFoundException(riskId));

        Risk updated = new Risk(
                existing.id(),
                valueOrDefault(command.riskFactor(), existing.riskFactor()),
                valueOrDefault(command.consequences(), existing.consequences()),
                valueOrDefault(command.impactNature(), existing.impactNature()),
                valueOrDefault(command.measurementMode(), existing.measurementMode()),
                valueOrDefault(command.initialImpactScale(), existing.initialImpactScale()),
                valueOrDefault(command.initialImpactEuros(), existing.initialImpactEuros()),
                valueOrDefault(command.initialProbabilityScale(), existing.initialProbabilityScale()),
                valueOrDefault(command.initialProbabilityValue(), existing.initialProbabilityValue()),
                valueOrDefault(command.actionId(), existing.actionId()),
                valueOrDefault(command.action(), existing.action()),
                valueOrDefault(command.actionNature(), existing.actionNature()),
                valueOrDefault(command.owner(), existing.owner()),
                valueOrDefault(command.mitigatedImpactScale(), existing.mitigatedImpactScale()),
                valueOrDefault(command.mitigatedImpactEuros(), existing.mitigatedImpactEuros()),
                valueOrDefault(command.correctedProbabilityScale(), existing.correctedProbabilityScale()),
                valueOrDefault(command.correctedProbabilityValue(), existing.correctedProbabilityValue())
        );

        return toDto(riskRepositoryPort.save(updated));
    }

    public void deleteRisk(Long riskId) {
        riskRepositoryPort.findById(riskId).orElseThrow(() -> new RiskNotFoundException(riskId));
        riskRepositoryPort.deleteById(riskId);
    }

    private Risk toDomain(Long id, CreateRiskCommand command) {
        return new Risk(
                id,
                command.riskFactor(),
                command.consequences(),
                command.impactNature(),
                command.measurementMode(),
                command.initialImpactScale(),
                command.initialImpactEuros(),
                command.initialProbabilityScale(),
                command.initialProbabilityValue(),
                command.actionId(),
                command.action(),
                command.actionNature(),
                command.owner(),
                command.mitigatedImpactScale(),
                command.mitigatedImpactEuros(),
                command.correctedProbabilityScale(),
                command.correctedProbabilityValue()
        );
    }

    private RiskDTO toDto(Risk risk) {
        return new RiskDTO(
                risk.id(),
                risk.riskFactor(),
                risk.consequences(),
                risk.impactNature(),
                risk.measurementMode(),
                risk.initialImpactScale(),
                risk.initialImpactEuros(),
                risk.initialProbabilityScale(),
                risk.initialProbabilityValue(),
                risk.actionId(),
                risk.action(),
                risk.actionNature(),
                risk.owner(),
                risk.mitigatedImpactScale(),
                risk.mitigatedImpactEuros(),
                risk.correctedProbabilityScale(),
                risk.correctedProbabilityValue()
        );
    }

    private <T> T valueOrDefault(T value, T fallback) {
        return value != null ? value : fallback;
    }
}