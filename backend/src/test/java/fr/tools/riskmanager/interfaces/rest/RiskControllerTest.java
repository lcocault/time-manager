package fr.tools.riskmanager.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tools.timemanager.interfaces.rest.RestExceptionHandler;
import fr.tools.riskmanager.application.dto.RiskDTO;
import fr.tools.riskmanager.application.service.RiskService;
import fr.tools.riskmanager.domain.model.RiskActionNature;
import fr.tools.riskmanager.domain.model.RiskMeasurementMode;
import fr.tools.riskmanager.domain.model.RiskOwner;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class RiskControllerTest {
    @Mock
    private RiskService riskService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateRiskThroughRestEndpoint() throws Exception {
        when(riskService.createRisk(any())).thenReturn(new RiskDTO(
                6L,
                "Retard partenaire",
                "Décalage recette",
                "Planning",
                RiskMeasurementMode.QUANTITATIVE,
                null,
                BigDecimal.valueOf(20000),
                null,
                BigDecimal.valueOf(0.7),
                16L,
                "Sécuriser un plan B",
                RiskActionNature.BOTH,
                RiskOwner.BOTH,
                null,
                BigDecimal.valueOf(9000),
                null,
                BigDecimal.valueOf(0.4)
        ));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new RiskController(riskService))
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        mockMvc.perform(post("/api/risks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateRiskPayload())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6L))
                .andExpect(jsonPath("$.measurementMode").value("QUANTITATIVE"));
    }

    private record CreateRiskPayload(
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
        private CreateRiskPayload() {
            this(
                    "Retard partenaire",
                    "Décalage recette",
                    "Planning",
                    RiskMeasurementMode.QUANTITATIVE,
                    null,
                    BigDecimal.valueOf(20000),
                    null,
                    BigDecimal.valueOf(0.7),
                    16L,
                    "Sécuriser un plan B",
                    RiskActionNature.BOTH,
                    RiskOwner.BOTH,
                    null,
                    BigDecimal.valueOf(9000),
                    null,
                    BigDecimal.valueOf(0.4)
            );
        }
    }
}