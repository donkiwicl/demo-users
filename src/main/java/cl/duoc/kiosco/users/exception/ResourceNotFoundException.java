package cl.duoc.kiosco.users.exception;

/**
 * Se lanza cuando un recurso solicitado no existe (por ejemplo, un usuario por id).
 * El {@link GlobalExceptionHandler} la traduce a una respuesta HTTP 404 (Not Found).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
