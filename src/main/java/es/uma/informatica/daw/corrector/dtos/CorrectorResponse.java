package es.uma.informatica.daw.corrector.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectorResponse {
    private Long id;
    private Long identificadorUsuario;
    private String telefono;
    private Integer maximasCorrecciones;
    private List<MateriaResponse> materias;
}
