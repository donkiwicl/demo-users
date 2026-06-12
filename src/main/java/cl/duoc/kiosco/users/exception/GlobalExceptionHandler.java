package cl.duoc.kiosco.users.exception;

import cl.duoc.kiosco.users.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones. Centraliza la traducción de excepciones de la
 * aplicación a respuestas HTTP con un cuerpo uniforme ({@link ExceptionDTO}).
 * Cada método @ExceptionHandler atiende un tipo de error y le asigna su código HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Recurso inexistente -> 404 Not Found. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Email duplicado -> 409 Conflict. */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleEmailExists(EmailAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Falla de validación de @Valid en el body -> 400 Bad Request. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");
        return build(HttpStatus.BAD_REQUEST, message);
    }

    /** Argumento inválido genérico -> 400 Bad Request. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Credenciales inválidas / no autenticado -> 401 Unauthorized. */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDTO> handleAuthentication(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
    }

    /** Sin permisos para el recurso -> 403 Forbidden. */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "No tienes permisos para acceder a este recurso");
    }

    /** Cualquier otra excepción no controlada -> 500 Internal Server Error. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<ExceptionDTO> build(HttpStatus status, String message) {
        return new ResponseEntity<>(new ExceptionDTO(status, message), status);
    }
}
