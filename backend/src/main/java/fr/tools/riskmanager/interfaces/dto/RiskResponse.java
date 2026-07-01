package fr.tools.riskmanager.interfaces.dto;

import fr.tools.riskmanager.application.dto.RiskDTO;
import fr.tools.riskmanager.domain.model.RiskActionNature;
import fr.tools.riskmanager.domain.model.RiskMeasurementMode;
import fr.tools.riskmanager.domain.model.RiskOwner;
import java.math.BigDecimal;

public record RiskResponse(
        Long id,
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
    public static RiskResponse from(RiskDTO risk) {
        return new RiskResponse(
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
}