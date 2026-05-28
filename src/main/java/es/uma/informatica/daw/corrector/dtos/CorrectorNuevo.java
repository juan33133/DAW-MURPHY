package es.uma.informatica.daw.corrector.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectorNuevo {
    @NotNull(message = "El identificador de usuario es obligatorio.")
    private Long identificadorUsuario;

    @NotNull(message = "El identificador de convocatoria es obligatorio.")
    private Long identificadorConvocatoria;

    @Pattern(regexp = "^([0-9]{9})?$", message = "El telefono es opcional o debe contener 9 digitos.")
    private String telefono;

    @NotNull(message = "La materia asociada es obligatoria.")
    private Materia materia;

    @NotNull(message = "El numero maximo de correcciones es obligatorio.")
    @Min(value = 1, message = "El corrector debe poder corregir al menos 1 examen.")
    private Integer maximasCorrecciones;
}
