package fr.tools.riskmanager.domain.model;

import java.math.BigDecimal;

public record Risk(
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
    public Risk {
        riskFactor = requireText(riskFactor, "riskFactor");
        consequences = requireText(consequences, "consequences");
        impactNature = requireText(impactNature, "impactNature");
        action = requireText(action, "action");
        actionId = requirePositive(actionId, "actionId");
        measurementMode = measurementMode == null ? RiskMeasurementMode.SCALE : measurementMode;
        actionNature = actionNature == null ? RiskActionNature.BOTH : actionNature;
        owner = owner == null ? RiskOwner.BOTH : owner;

        if (measurementMode == RiskMeasurementMode.SCALE) {
            initialImpactScale = requireScale(initialImpactScale, "initialImpactScale");
            initialProbabilityScale = requireScale(initialProbabilityScale, "initialProbabilityScale");
            mitigatedImpactScale = requireScale(mitigatedImpactScale, "mitigatedImpactScale");
            correctedProbabilityScale = requireScale(correctedProbabilityScale, "correctedProbabilityScale");

            requireNull(initialImpactEuros, "initialImpactEuros");
            requireNull(initialProbabilityValue, "initialProbabilityValue");
            requireNull(mitigatedImpactEuros, "mitigatedImpactEuros");
            requireNull(correctedProbabilityValue, "correctedProbabilityValue");
        } else {
            initialImpactEuros = requirePositiveOrZero(initialImpactEuros, "initialImpactEuros");
            mitigatedImpactEuros = requirePositiveOrZero(mitigatedImpactEuros, "mitigatedImpactEuros");
            initialProbabilityValue = requireProbability(initialProbabilityValue, "initialProbabilityValue");
            correctedProbabilityValue = requireProbability(correctedProbabilityValue, "correctedProbabilityValue");

            requireNull(initialImpactScale, "initialImpactScale");
            requireNull(initialProbabilityScale, "initialProbabilityScale");
            requireNull(mitigatedImpactScale, "mitigatedImpactScale");
            requireNull(correctedProbabilityScale, "correctedProbabilityScale");
        }
    }

    private String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    private Long requirePositive(Long value, String field) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(field + " must be greater than 0");
        }
        return value;
    }

    private Integer requireScale(Integer value, String field) {
        if (value == null || value < 1 || value > 5) {
            throw new IllegalArgumentException(field + " must be between 1 and 5");
        }
        return value;
    }

    private BigDecimal requireProbability(BigDecimal value, String field) {
        if (value == null
                || value.compareTo(BigDecimal.ZERO) < 0
                || value.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException(field + " must be between 0 and 1");
        }
        return value;
    }

    private BigDecimal requirePositiveOrZero(BigDecimal value, String field) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(field + " must be greater than or equal to 0");
        }
        return value;
    }

    private void requireNull(Object value, String field) {
        if (value != null) {
            throw new IllegalArgumentException(field + " must be null for selected measurement mode");
        }
    }
}