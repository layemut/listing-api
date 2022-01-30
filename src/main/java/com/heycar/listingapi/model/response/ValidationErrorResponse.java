package com.heycar.listingapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.ConstraintViolation;
import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    List<Violation> violations;

    @Data
    public static class Violation {
        private String field;
        private String message;

        public Violation(ConstraintViolation violation) {
            this.field = violation.getPropertyPath().toString();
            this.message = violation.getMessage();
        }

        public Violation(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
