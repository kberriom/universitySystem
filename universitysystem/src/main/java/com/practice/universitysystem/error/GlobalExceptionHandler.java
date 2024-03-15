package com.practice.universitysystem.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoSuchElementException.class})
    protected ResponseEntity<Object> handleGenericNotFound(RuntimeException ex, WebRequest webRequest) {
        return manageError(ex, HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleGenericInvalid(RuntimeException ex, WebRequest webRequest) {
        return manageError(ex, HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    protected ResponseEntity<Object> handleGenericConflict(RuntimeException ex, WebRequest webRequest) {
        return manageError(ex, HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {

        Map<String, String> parsedErrors = new HashMap<>();
        ex.getConstraintViolations().iterator().
                forEachRemaining(constraintViolation -> parsedErrors.put(String.valueOf(constraintViolation.getPropertyPath()), constraintViolation.getMessage()));

        return manageError(ex, HttpStatus.BAD_REQUEST, webRequest, getExceptionName(ex), parsedErrors.toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest webRequest) {
        String s = ex.getLocalizedMessage();
        String msg;
        try {
            msg = s.substring(s.indexOf("constraint"), s.lastIndexOf(";"));
        } catch (Exception e) {
            msg = ex.getLocalizedMessage();
        }
        return manageError(ex, HttpStatus.CONFLICT, webRequest, getExceptionName(ex), msg);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return manageError(ex, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return manageError(ex, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String msg = ex.getLocalizedMessage();
        try {
            msg = msg.substring(0, msg.indexOf(":"));
        } catch (Exception e) {
            msg = ex.getLocalizedMessage();
        }
        return manageError(ex, HttpStatus.BAD_REQUEST, request, "messageNotReadable", msg);
    }

    private String getExceptionName(Exception ex) {
        String exClass = ex.toString();
        try {
            int endIndex = exClass.indexOf(":");
            return exClass.substring((exClass.substring(0, endIndex).lastIndexOf(".")+1), endIndex);
        } catch (Exception e) {
            return exClass;
        }
    }

    private ResponseEntity<Object> manageError(Exception ex, HttpStatus responseCode, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(responseCode);
        response.setMessage(ex.getLocalizedMessage());
        response.setErrorName(getExceptionName(ex));
        return handleExceptionInternal(ex, response, new HttpHeaders(), responseCode, request);
    }

    private ResponseEntity<Object> manageError(Exception ex, HttpStatus responseCode, WebRequest request,
                                                             String errorName, String msg) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(responseCode);
        response.setMessage(msg);
        response.setErrorName(errorName);
        return handleExceptionInternal(ex, response, new HttpHeaders(), responseCode, request);
    }
}
