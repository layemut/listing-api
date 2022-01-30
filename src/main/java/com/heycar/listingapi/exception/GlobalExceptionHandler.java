package com.heycar.listingapi.exception;

import com.heycar.listingapi.model.response.InternalErrorResponse;
import com.heycar.listingapi.model.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        var violations = constraintViolationException.getConstraintViolations()
                .stream()
                .map(ValidationErrorResponse.Violation::new)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler(value = InvalidCSVException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleInvalidCSVException() {
        var violations = List.of(new ValidationErrorResponse.Violation("file", "Invalid or corrupt csv file"));
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<InternalErrorResponse> handleException() {
        return ResponseEntity.internalServerError().body(new InternalErrorResponse("Something went wrong on our side, we will be notified of this issue and fix it as soon as possible"));
    }
}
