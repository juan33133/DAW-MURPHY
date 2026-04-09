package es.uma.informatica.daw.corrector.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * DTO used when creating a new Corrector via the API.
 * Mirrors the OpenAPI schema 'CorrectorNuevo'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrectorNuevo {
    /**
     * Identificador del usuario que será el corrector.
     */
    private Long identificadorUsuario;

    /**
     * Identificador de la convocatoria a la que pertenece el corrector.
     */
    private Long identificadorConvocatoria;

    /**
     * Teléfono de contacto del corrector.
     */
    private String telefono;

    /**
     * Materia que el corrector podrá corregir.
     * This is a reference to the {@link Materia} entity.
     */
    private Materia materia;

    /**
     * Número máximo de correcciones que el corrector puede realizar.
     */
    private Integer maximasCorrecciones;
}
