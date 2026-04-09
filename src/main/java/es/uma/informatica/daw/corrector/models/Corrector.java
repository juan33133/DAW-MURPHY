package es.uma.informatica.daw.corrector.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "correctores")
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
    private Set<MateriaEnConvocatoria> materias;
}
