package com.endava.marketplace.backend.advice;

import com.endava.marketplace.backend.exception.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Hidden
public class ApplicationExceptionHandler {
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {EntityAlreadyExistsException.class})
    public Map<String, String> entityAlreadyExistsException(EntityAlreadyExistsException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public Map<String, String> entityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {EntityAttributeAlreadySetException.class})
    public Map<String, String> entityAttributeAlreadySetException(EntityAttributeAlreadySetException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {NoAttributeDefinedException.class})
    public Map<String, String> noAttributeDefinedException(NoAttributeDefinedException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {InvalidStatusException.class})
    public Map<String, String> invalidStatusException(InvalidStatusException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {NotEnoughPermissionsException.class})
    public Map<String, String> notEnoughPermissionsException(NotEnoughPermissionsException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {InsufficientStockException.class})
    public Map<String, String> insufficientStockException(InsufficientStockException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String, String> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> constraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getConstraintViolations().forEach(constraintViolation -> errorMap.put(constraintViolation.getPropertyPath().toString().split("\\.")[1] + " error", constraintViolation.getMessage()));
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Map<String, String> maxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidContentTypeException.class)
    public Map<String, String> invalidContentTypeException(InvalidContentTypeException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }
}
