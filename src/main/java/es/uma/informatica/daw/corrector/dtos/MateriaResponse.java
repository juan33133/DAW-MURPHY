package es.uma.informatica.daw.corrector.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MateriaResponse {
    private Long idMateria;
    private Long idConvocatoria;
}
