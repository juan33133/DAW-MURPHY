package es.uma.informatica.daw.corrector.excepciones;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(CorrectorNoEncontradoException.class)
    public ResponseEntity<String> handleCorrectorNoEncontrado(CorrectorNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<String> handleUsuarioYaExiste(UsuarioYaExisteException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // Captura los fallos del @Valid del DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidaciones(MethodArgumentNotValidException e) {
        String errores = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERR -> " + errores);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no autorizado.");
    }
}
