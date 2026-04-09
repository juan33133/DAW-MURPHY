package es.uma.informatica.daw.corrector.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "materias")
@Data
public class Materia {
    @Id
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
