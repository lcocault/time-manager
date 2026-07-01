package fr.tools.riskmanager.infrastructure.persistence;

import fr.tools.riskmanager.domain.model.RiskActionNature;
import fr.tools.riskmanager.domain.model.RiskMeasurementMode;
import fr.tools.riskmanager.domain.model.RiskOwner;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "risk")
public class RiskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "risk_factor", nullable = false)
    private String riskFactor;

    @Column(nullable = false)
    private String consequences;

    @Column(name = "impact_nature", nullable = false)
    private String impactNature;

    @Enumerated(EnumType.STRING)
    @Column(name = "measurement_mode", nullable = false)
    private RiskMeasurementMode measurementMode;

    @Column(name = "initial_impact_scale")
    private Integer initialImpactScale;

    @Column(name = "initial_impact_euros", precision = 15, scale = 2)
    private BigDecimal initialImpactEuros;

    @Column(name = "initial_probability_scale")
    private Integer initialProbabilityScale;

    @Column(name = "initial_probability_value", precision = 8, scale = 6)
    private BigDecimal initialProbabilityValue;

    @Column(name = "action_id", nullable = false)
    private Long actionId;

    @Column(nullable = false)
    private String action;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_nature", nullable = false)
    private RiskActionNature actionNature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskOwner owner;

    @Column(name = "mitigated_impact_scale")
    private Integer mitigatedImpactScale;

    @Column(name = "mitigated_impact_euros", precision = 15, scale = 2)
    private BigDecimal mitigatedImpactEuros;

    @Column(name = "corrected_probability_scale")
    private Integer correctedProbabilityScale;

    @Column(name = "corrected_probability_value", precision = 8, scale = 6)
    private BigDecimal correctedProbabilityValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(String riskFactor) {
        this.riskFactor = riskFactor;
    }

    public String getConsequences() {
        return consequences;
    }

    public void setConsequences(String consequences) {
        this.consequences = consequences;
    }

    public String getImpactNature() {
        return impactNature;
    }

    public void setImpactNature(String impactNature) {
        this.impactNature = impactNature;
    }

    public RiskMeasurementMode getMeasurementMode() {
        return measurementMode;
    }

    public void setMeasurementMode(RiskMeasurementMode measurementMode) {
        this.measurementMode = measurementMode;
    }

    public Integer getInitialImpactScale() {
        return initialImpactScale;
    }

    public void setInitialImpactScale(Integer initialImpactScale) {
        this.initialImpactScale = initialImpactScale;
    }

    public BigDecimal getInitialImpactEuros() {
        return initialImpactEuros;
    }

    public void setInitialImpactEuros(BigDecimal initialImpactEuros) {
        this.initialImpactEuros = initialImpactEuros;
    }

    public Integer getInitialProbabilityScale() {
        return initialProbabilityScale;
    }

    public void setInitialProbabilityScale(Integer initialProbabilityScale) {
        this.initialProbabilityScale = initialProbabilityScale;
    }

    public BigDecimal getInitialProbabilityValue() {
        return initialProbabilityValue;
    }

    public void setInitialProbabilityValue(BigDecimal initialProbabilityValue) {
        this.initialProbabilityValue = initialProbabilityValue;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public RiskActionNature getActionNature() {
        return actionNature;
    }

    public void setActionNature(RiskActionNature actionNature) {
        this.actionNature = actionNature;
    }

    public RiskOwner getOwner() {
        return owner;
    }

    public void setOwner(RiskOwner owner) {
        this.owner = owner;
    }

    public Integer getMitigatedImpactScale() {
        return mitigatedImpactScale;
    }

    public void setMitigatedImpactScale(Integer mitigatedImpactScale) {
        this.mitigatedImpactScale = mitigatedImpactScale;
    }

    public BigDecimal getMitigatedImpactEuros() {
        return mitigatedImpactEuros;
    }

    public void setMitigatedImpactEuros(BigDecimal mitigatedImpactEuros) {
        this.mitigatedImpactEuros = mitigatedImpactEuros;
    }

    public Integer getCorrectedProbabilityScale() {
        return correctedProbabilityScale;
    }

    public void setCorrectedProbabilityScale(Integer correctedProbabilityScale) {
        this.correctedProbabilityScale = correctedProbabilityScale;
    }

    public BigDecimal getCorrectedProbabilityValue() {
        return correctedProbabilityValue;
    }

    public void setCorrectedProbabilityValue(BigDecimal correctedProbabilityValue) {
        this.correctedProbabilityValue = correctedProbabilityValue;
    }
}