package es.uma.informatica.daw.corrector.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "correctores")
@Data
@AllArgsConstructor
public class Corrector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identificador_usuario")
    private Long identificadorUsuario;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "maximas_correcciones")
    private Integer maximasCorrecciones;

    @OneToMany
    @JoinColumn(name = "id_corrector")
    private Set<MateriaEnConvocatoria> materias = new HashSet<>();
}
