package es.uma.informatica.daw.corrector.models;

import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "materias_en_convocatoria")
public class MateriaEnConvocatoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_materia")
    private Long idMateria;

    @Column(name = "id_convocatoria")
    private Long idConvocatoria;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdMateria() { return idMateria; }
    public void setIdMateria(Long idMateria) { this.idMateria = idMateria; }
    public Long getIdConvocatoria() { return idConvocatoria; }
    public void setIdConvocatoria(Long idConvocatoria) { this.idConvocatoria = idConvocatoria; }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MateriaEnConvocatoria that = (MateriaEnConvocatoria) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(idMateria, that.idMateria) &&
               Objects.equals(idConvocatoria, that.idConvocatoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idMateria, idConvocatoria);
    }

    @Override
    public String toString() {
        return "MateriaEnConvocatoria{" +
               "id=" + id +
               ", idMateria=" + idMateria +
               ", idConvocatoria=" + idConvocatoria +
               '}';
    }
}
