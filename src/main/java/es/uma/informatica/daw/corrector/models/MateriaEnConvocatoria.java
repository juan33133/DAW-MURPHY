package es.uma.informatica.daw.corrector.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "materias_en_convocatoria",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"id_corrector", "id_convocatoria"})})
@Getter @Setter
@NoArgsConstructor
public class MateriaEnConvocatoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "id_materia", nullable = false)
    private Long idMateria;

    @Column(name = "id_convocatoria", nullable = false)
    private Long idConvocatoria;

    @ManyToOne
    @JoinColumn(name = "id_corrector")
    @JsonIgnore
    private Corrector corrector;

    public MateriaEnConvocatoria(Long idMateria, Long idConvocatoria, Corrector corrector) {
        this.idMateria = idMateria;
        this.idConvocatoria = idConvocatoria;
        this.corrector = corrector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MateriaEnConvocatoria)) return false;
        MateriaEnConvocatoria that = (MateriaEnConvocatoria) o;
        return Objects.equals(idMateria, that.idMateria) &&
               Objects.equals(idConvocatoria, that.idConvocatoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMateria, idConvocatoria);
    }
}
