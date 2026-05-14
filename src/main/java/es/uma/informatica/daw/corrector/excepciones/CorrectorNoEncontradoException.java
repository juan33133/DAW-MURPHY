package es.uma.informatica.daw.corrector.excepciones;

public class CorrectorNoEncontradoException extends RuntimeException {
    public CorrectorNoEncontradoException(String message) {
        super(message);
    }
}
