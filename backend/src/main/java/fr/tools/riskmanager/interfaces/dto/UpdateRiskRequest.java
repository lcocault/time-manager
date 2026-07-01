package fr.tools.riskmanager.interfaces.dto;

import fr.tools.riskmanager.application.dto.UpdateRiskCommand;
import fr.tools.riskmanager.domain.model.RiskActionNature;
import fr.tools.riskmanager.domain.model.RiskMeasurementMode;
import fr.tools.riskmanager.domain.model.RiskOwner;
import java.math.BigDecimal;

public record UpdateRiskRequest(
        String riskFactor,
        String consequences,
        String impactNature,
        RiskMeasurementMode measurementMode,
        Integer initialImpactScale,
        BigDecimal initialImpactEuros,
        Integer initialProbabilityScale,
        BigDecimal initialProbabilityValue,
        Long actionId,
        String action,
        RiskActionNature actionNature,
        RiskOwner owner,
        Integer mitigatedImpactScale,
        BigDecimal mitigatedImpactEuros,
        Integer correctedProbabilityScale,
        BigDecimal correctedProbabilityValue
) {
    public UpdateRiskCommand toCommand() {
        return new UpdateRiskCommand(
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