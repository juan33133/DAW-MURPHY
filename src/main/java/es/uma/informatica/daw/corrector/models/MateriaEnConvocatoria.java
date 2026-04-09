package es.uma.informatica.daw.corrector.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "materias_en_convocatoria")
@Data
public class MateriaEnConvocatoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_materia")
    private Long idMateria;

    @Column(name = "id_convocatoria")
    private Long idConvocatoria;
}
