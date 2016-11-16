package com.rnaufal.address.management.endpoint;

import com.rnaufal.address.management.exception.AddressException;
import com.rnaufal.address.management.exception.AddressNotFoundException;
import com.rnaufal.address.management.exception.CepNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rnaufal on 14/11/16.
 */
@ControllerAdvice
@Component
public class DefaultExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Value("${default.error.message}")
    private String defaultErrorMessage;

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<List<ResponseMessage>> handleValidationErrors(ConstraintViolationException ex) {
        List<ResponseMessage> errors = ex
                .getConstraintViolations()
                .stream()
                .map(constraintViolation -> new ResponseMessage(constraintViolation.getMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({CepNotFoundException.class,
            AddressNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ResponseMessage> handleException(AddressException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<List<ResponseMessage>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ResponseMessage> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ResponseMessage(error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleUnexpectedError(Exception ex) {
        LOGGER.error("Error: ", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessage(defaultErrorMessage));
    }
}
