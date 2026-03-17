package com.joboffers.domain;

import com.joboffers.domain.loginandregister.UserAlreadyExistsException;
import com.joboffers.domain.loginandregister.UserNotFoundException;
import com.joboffers.domain.offer.OfferNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessageResponse> handleOfferNotFoundException(OfferNotFoundException e) {
        ErrorMessageResponse error = new ErrorMessageResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessageResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorMessageResponse error = new ErrorMessageResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorMessageResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        String message = "Offer with this URL already exists";
        ErrorMessageResponse error = new ErrorMessageResponse(message, HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ErrorMessageResponse error = new ErrorMessageResponse(e.getMessage(), HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
