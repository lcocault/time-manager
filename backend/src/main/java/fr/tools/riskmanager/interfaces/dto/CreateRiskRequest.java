package fr.tools.riskmanager.interfaces.dto;

import fr.tools.riskmanager.application.dto.CreateRiskCommand;
import fr.tools.riskmanager.domain.model.RiskActionNature;
import fr.tools.riskmanager.domain.model.RiskMeasurementMode;
import fr.tools.riskmanager.domain.model.RiskOwner;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateRiskRequest(
        @NotBlank String riskFactor,
        @NotBlank String consequences,
        @NotBlank String impactNature,
        @NotNull RiskMeasurementMode measurementMode,
        Integer initialImpactScale,
        BigDecimal initialImpactEuros,
        Integer initialProbabilityScale,
        BigDecimal initialProbabilityValue,
        @NotNull @Positive Long actionId,
        @NotBlank String action,
        RiskActionNature actionNature,
        RiskOwner owner,
        Integer mitigatedImpactScale,
        BigDecimal mitigatedImpactEuros,
        Integer correctedProbabilityScale,
        BigDecimal correctedProbabilityValue
) {
    public CreateRiskCommand toCommand() {
        return new CreateRiskCommand(
                riskFactor,
                consequences,
                impactNature,
                measurementMode,
                initialImpactScale,
                initialImpactEuros,
                initialProbabilityScale,
                initialProbabilityValue,
                actionId,
                action,
                actionNature,
                owner,
                mitigatedImpactScale,
                mitigatedImpactEuros,
                correctedProbabilityScale,
                correctedProbabilityValue
        );
    }
}