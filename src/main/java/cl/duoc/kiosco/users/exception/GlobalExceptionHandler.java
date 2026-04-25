package cl.duoc.kiosco.users.exception;

import cl.duoc.kiosco.users.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(NoSuchElementException ex){
        ExceptionDTO exceptionDTO = new ExceptionDTO(
                HttpStatus.NOT_FOUND,
                ex
        );
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(SQLIntegrityConstraintViolationException ex){
        ExceptionDTO exceptionDTO = new ExceptionDTO(
                HttpStatus.CONFLICT,
                ex
        );
        return new ResponseEntity<>(exceptionDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(Exception ex){
        ExceptionDTO exceptionDTO = new ExceptionDTO(
                HttpStatus.BAD_REQUEST,
                ex
        );
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }
}
