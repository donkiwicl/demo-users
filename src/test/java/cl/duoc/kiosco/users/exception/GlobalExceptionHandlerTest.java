package cl.duoc.kiosco.users.exception;

import cl.duoc.kiosco.users.dto.ExceptionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de {@link GlobalExceptionHandler}: cada handler debe traducir
 * su excepción al código HTTP correcto y exponer el mensaje en {@link ExceptionDTO}.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_devuelve404() {
        ResponseEntity<ExceptionDTO> response =
                handler.handleNotFound(new ResourceNotFoundException("Usuario no encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuario no encontrado", response.getBody().getMessage());
        assertEquals(404, response.getBody().getCode());
    }

    @Test
    void handleEmailExists_devuelve409() {
        ResponseEntity<ExceptionDTO> response =
                handler.handleEmailExists(new EmailAlreadyExistsException("El email ya está registrado"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El email ya está registrado", response.getBody().getMessage());
    }

    @Test
    void handleValidation_devuelve400ConPrimerError() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(new FieldError("user", "email", "no debe estar vacío")));

        ResponseEntity<ExceptionDTO> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("email: no debe estar vacío", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgument_devuelve400() {
        ResponseEntity<ExceptionDTO> response =
                handler.handleIllegalArgument(new IllegalArgumentException("Argumento inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Argumento inválido", response.getBody().getMessage());
    }

    @Test
    void handleAuthentication_devuelve401() {
        ResponseEntity<ExceptionDTO> response =
                handler.handleAuthentication(new BadCredentialsException("bad"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales inválidas", response.getBody().getMessage());
    }

    @Test
    void handleAccessDenied_devuelve403() {
        ResponseEntity<ExceptionDTO> response =
                handler.handleAccessDenied(new AccessDeniedException("denied"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("No tienes permisos para acceder a este recurso", response.getBody().getMessage());
    }

    @Test
    void handleGenericException_devuelve500() {
        ResponseEntity<ExceptionDTO> response =
                handler.handleGenericException(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
    }
}
