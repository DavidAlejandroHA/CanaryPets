package com.canarypets.backend.exceptions;

import com.canarypets.backend.DTOs.FieldErrorDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<FieldErrorDTO> errors = new ArrayList<>();

    public void addError(String field, String message) {
        errors.add(new FieldErrorDTO(field, "error." + field, message));
    }

    public List<FieldErrorDTO> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}