package es.uma.informatica.daw.corrector.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "correctores")
@Getter @Setter
@NoArgsConstructor
public class Corrector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identificador_usuario", unique = true, nullable = false)
    private Long identificadorUsuario;

    private String telefono;

    @Column(name = "maximas_correcciones", nullable = false)
    private Integer maximasCorrecciones;

    @OneToMany(mappedBy = "corrector", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MateriaEnConvocatoria> materias = new HashSet<>();

    public Corrector(Long identificadorUsuario, String telefono, Integer maximasCorrecciones) {
        this.identificadorUsuario = identificadorUsuario;
        this.telefono = telefono;
        this.maximasCorrecciones = maximasCorrecciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Corrector)) return false;
        Corrector corrector = (Corrector) o;
        return Objects.equals(identificadorUsuario, corrector.identificadorUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificadorUsuario);
    }
}
