package fr.tools.riskmanager.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RiskTest {
    @Test
    void shouldCreateScaleRiskWithValuesBetweenOneAndFive() {
        Risk risk = new Risk(
                1L,
                "Retard fournisseur",
                "Décalage planning",
                "Charge supplémentaire",
                RiskMeasurementMode.SCALE,
                4,
                null,
                3,
                null,
                12L,
                "Replanifier avec marges",
                RiskActionNature.BOTH,
                RiskOwner.BOTH,
                2,
                null,
                2,
                null
        );

        assertEquals(4, risk.initialImpactScale());
        assertEquals(2, risk.mitigatedImpactScale());
    }

    @Test
    void shouldRejectQuantitativeRiskWithProbabilityOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new Risk(
                1L,
                "Risque financier",
                "Dépassement budget",
                "Coût projet",
                RiskMeasurementMode.QUANTITATIVE,
                null,
                BigDecimal.valueOf(10000),
                null,
                BigDecimal.valueOf(1.2),
                42L,
                "Sécuriser les achats",
                RiskActionNature.AVOID,
                RiskOwner.CNES,
                null,
                BigDecimal.valueOf(5000),
                null,
                BigDecimal.valueOf(0.4)
        ));
    }
}