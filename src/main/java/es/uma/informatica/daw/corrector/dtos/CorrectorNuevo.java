package es.uma.informatica.daw.corrector.dtos;

import es.uma.informatica.daw.corrector.models.Materia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectorNuevo {
    private Long identificadorUsuario;
    private Long identificadorConvocatoria;
    private String telefono;
    private Materia materia;
    private Integer maximasCorrecciones;
}
