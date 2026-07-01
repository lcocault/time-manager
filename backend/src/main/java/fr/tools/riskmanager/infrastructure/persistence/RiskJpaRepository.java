package fr.tools.riskmanager.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskJpaRepository extends JpaRepository<RiskEntity, Long> {
}