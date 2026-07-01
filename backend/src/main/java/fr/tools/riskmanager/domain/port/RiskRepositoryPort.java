package fr.tools.riskmanager.domain.port;

import fr.tools.riskmanager.domain.model.Risk;
import java.util.List;
import java.util.Optional;

public interface RiskRepositoryPort {
    Risk save(Risk risk);

    List<Risk> findAll();

    Optional<Risk> findById(Long id);

    void deleteById(Long id);
}