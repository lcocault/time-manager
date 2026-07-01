package fr.tools.riskmanager.infrastructure.persistence;

import fr.tools.riskmanager.domain.model.Risk;
import fr.tools.riskmanager.domain.port.RiskRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RiskPersistenceAdapter implements RiskRepositoryPort {
    private final RiskJpaRepository riskJpaRepository;

    public RiskPersistenceAdapter(RiskJpaRepository riskJpaRepository) {
        this.riskJpaRepository = riskJpaRepository;
    }

    @Override
    public Risk save(Risk risk) {
        return toDomain(riskJpaRepository.save(toEntity(risk)));
    }

    @Override
    public List<Risk> findAll() {
        return riskJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Risk> findById(Long id) {
        return riskJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        riskJpaRepository.deleteById(id);
    }

    private RiskEntity toEntity(Risk risk) {
        RiskEntity entity = new RiskEntity();
        entity.setId(risk.id());
        entity.setRiskFactor(risk.riskFactor());
        entity.setConsequences(risk.consequences());
        entity.setImpactNature(risk.impactNature());
        entity.setMeasurementMode(risk.measurementMode());
        entity.setInitialImpactScale(risk.initialImpactScale());
        entity.setInitialImpactEuros(risk.initialImpactEuros());
        entity.setInitialProbabilityScale(risk.initialProbabilityScale());
        entity.setInitialProbabilityValue(risk.initialProbabilityValue());
        entity.setActionId(risk.actionId());
        entity.setAction(risk.action());
        entity.setActionNature(risk.actionNature());
        entity.setOwner(risk.owner());
        entity.setMitigatedImpactScale(risk.mitigatedImpactScale());
        entity.setMitigatedImpactEuros(risk.mitigatedImpactEuros());
        entity.setCorrectedProbabilityScale(risk.correctedProbabilityScale());
        entity.setCorrectedProbabilityValue(risk.correctedProbabilityValue());
        return entity;
    }

    private Risk toDomain(RiskEntity entity) {
        return new Risk(
                entity.getId(),
                entity.getRiskFactor(),
                entity.getConsequences(),
                entity.getImpactNature(),
                entity.getMeasurementMode(),
                entity.getInitialImpactScale(),
                entity.getInitialImpactEuros(),
                entity.getInitialProbabilityScale(),
                entity.getInitialProbabilityValue(),
                entity.getActionId(),
                entity.getAction(),
                entity.getActionNature(),
                entity.getOwner(),
                entity.getMitigatedImpactScale(),
                entity.getMitigatedImpactEuros(),
                entity.getCorrectedProbabilityScale(),
                entity.getCorrectedProbabilityValue()
        );
    }
}