package es.uma.informatica.daw.corrector.dtos;

import es.uma.informatica.daw.corrector.models.Materia;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CorrectorNuevo {
    private Long identificadorUsuario;
    private Long identificadorConvocatoria;
    private String telefono;
    private Integer maximasCorrecciones;

    private Materia materia;
}
