package fr.tools.timemanager.interfaces.rest;

import fr.tools.timemanager.domain.exception.TaskNotFoundException;
import fr.tools.riskmanager.domain.exception.RiskNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(TaskNotFoundException exception) {
        return Map.of("message", exception.getMessage());
    }

    @ExceptionHandler(RiskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleRiskNotFound(RiskNotFoundException exception) {
        return Map.of("message", exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequest(Exception exception) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("message", "Validation failed");
        if (exception instanceof MethodArgumentNotValidException validationException) {
            payload.put("errors", validationException.getBindingResult().getFieldErrors().stream()
                    .collect(LinkedHashMap::new,
                            (map, error) -> map.put(error.getField(), defaultMessage(error)),
                            LinkedHashMap::putAll));
        } else {
            payload.put("errors", Map.of("request", exception.getMessage()));
        }
        return payload;
    }

    private String defaultMessage(FieldError error) {
        return error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage();
    }
}
