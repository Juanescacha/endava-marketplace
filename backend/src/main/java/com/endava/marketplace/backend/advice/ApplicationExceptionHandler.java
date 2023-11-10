package com.endava.marketplace.backend.advice;

import com.endava.marketplace.backend.exception.BlankListingCategoryName;
import com.endava.marketplace.backend.exception.ListingCategoryAlreadyActive;
import com.endava.marketplace.backend.exception.ListingCategoryAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {ListingCategoryAlreadyExists.class})
    public Map<String, String> listingCategoryAlreadyExists(ListingCategoryAlreadyExists ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {ListingCategoryAlreadyActive.class})
    public Map<String, String> listingCategoryAlreadyActive(ListingCategoryAlreadyActive ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BlankListingCategoryName.class})
    public Map<String, String> blankListingCategoryName(BlankListingCategoryName ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NullPointerException.class})
    public Map<String, String> nullPointerException(NullPointerException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }
}
