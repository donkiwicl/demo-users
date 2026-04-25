package cl.duoc.kiosco.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(NoSuchElementException ex){
        Map<String,String> error = new HashMap<>();
        error.put("date",(new Date()).toString());
        error.put("message",ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(SQLIntegrityConstraintViolationException ex){
        Map<String,String> error = new HashMap<>();
        error.put("date",(new Date()).toString());
        error.put("message",ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleNotFound(Exception ex){
        Map<String,String> error = new HashMap<>();
        error.put("date",(new Date()).toString());
        error.put("message",ex.getMessage());
        error.put("class",ex.getClass().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
