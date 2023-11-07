package com.endava.marketplace.backend.advice;

import com.endava.marketplace.backend.exception.BlankListingCategory;
import com.endava.marketplace.backend.exception.ListingCategoryAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ListingCategoryExceptionHandler {
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {ListingCategoryAlreadyExists.class})
    public Map<String, String> listingCategoryAlreadyExists(ListingCategoryAlreadyExists ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BlankListingCategory.class})
    public Map<String, String> blankListingCategory(BlankListingCategory ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }
}
