package es.uma.informatica.daw.corrector.excepciones;

public class CorrectorNoEncontrado extends RuntimeException {
    public CorrectorNoEncontrado() {
        super("Corrector no encontrado");
    }

}
