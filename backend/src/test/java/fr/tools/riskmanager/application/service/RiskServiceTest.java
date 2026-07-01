package fr.tools.riskmanager.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.tools.riskmanager.application.dto.CreateRiskCommand;
import fr.tools.riskmanager.application.dto.UpdateRiskCommand;
import fr.tools.riskmanager.domain.model.Risk;
import fr.tools.riskmanager.domain.model.RiskActionNature;
import fr.tools.riskmanager.domain.model.RiskMeasurementMode;
import fr.tools.riskmanager.domain.model.RiskOwner;
import fr.tools.riskmanager.domain.port.RiskRepositoryPort;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RiskServiceTest {
    @Mock
    private RiskRepositoryPort riskRepositoryPort;

    @InjectMocks
    private RiskService riskService;

    @Test
    void shouldCreateRiskInScaleMode() {
        when(riskRepositoryPort.save(any(Risk.class))).thenAnswer(invocation -> {
            Risk risk = invocation.getArgument(0);
            return new Risk(
                    7L,
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
        });

        var createdRisk = riskService.createRisk(new CreateRiskCommand(
                "Retard de livraison",
                "Décalage des jalons",
                "Planning",
                RiskMeasurementMode.SCALE,
                4,
                null,
                3,
                null,
                101L,
                "Sécuriser un fournisseur alternatif",
                RiskActionNature.BOTH,
                RiskOwner.CAPGEMINI,
                2,
                null,
                2,
                null
        ));

        assertEquals(7L, createdRisk.id());
        assertEquals(RiskMeasurementMode.SCALE, createdRisk.measurementMode());
        assertEquals(4, createdRisk.initialImpactScale());
    }

    @Test
    void shouldUpdateRiskWithQuantitativeValues() {
        Risk existing = new Risk(
                8L,
                "Budget sous-estimé",
                "Overrun",
                "Coûts",
                RiskMeasurementMode.QUANTITATIVE,
                null,
                BigDecimal.valueOf(20000),
                null,
                BigDecimal.valueOf(0.6),
                202L,
                "Renforcer le contrôle des dépenses",
                RiskActionNature.MITIGATE,
                RiskOwner.BOTH,
                null,
                BigDecimal.valueOf(10000),
                null,
                BigDecimal.valueOf(0.4)
        );
        when(riskRepositoryPort.findById(8L)).thenReturn(Optional.of(existing));
        when(riskRepositoryPort.save(any(Risk.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var updated = riskService.updateRisk(8L, new UpdateRiskCommand(
                null,
                null,
                null,
                null,
                null,
                BigDecimal.valueOf(15000),
                null,
                BigDecimal.valueOf(0.5),
                null,
                null,
                null,
                null,
                null,
                BigDecimal.valueOf(7000),
                null,
                BigDecimal.valueOf(0.3)
        ));

        assertEquals(BigDecimal.valueOf(15000), updated.initialImpactEuros());
        assertEquals(BigDecimal.valueOf(0.3), updated.correctedProbabilityValue());
        verify(riskRepositoryPort).save(any(Risk.class));
    }
}