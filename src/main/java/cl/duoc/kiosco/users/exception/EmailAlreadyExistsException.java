package cl.duoc.kiosco.users.exception;

/**
 * Se lanza al intentar registrar un email que ya existe en la base de datos.
 * El {@link GlobalExceptionHandler} la traduce a una respuesta HTTP 409 (Conflict).
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
