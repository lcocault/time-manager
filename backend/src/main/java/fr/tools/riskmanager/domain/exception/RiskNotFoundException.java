package fr.tools.riskmanager.domain.exception;

public class RiskNotFoundException extends RuntimeException {
    public RiskNotFoundException(Long id) {
        super("Risk with id " + id + " was not found");
    }
}