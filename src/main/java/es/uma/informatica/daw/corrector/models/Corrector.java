package es.uma.informatica.daw.corrector.models;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

@Entity
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

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdentificadorUsuario() { return identificadorUsuario; }
    public void setIdentificadorUsuario(Long identificadorUsuario) { this.identificadorUsuario = identificadorUsuario; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Integer getMaximasCorrecciones() { return maximasCorrecciones; }
    public void setMaximasCorrecciones(Integer maximasCorrecciones) { this.maximasCorrecciones = maximasCorrecciones; }
    public Set<MateriaEnConvocatoria> getMaterias() { return materias; }
    public void setMaterias(Set<MateriaEnConvocatoria> materias) { this.materias = materias; }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corrector that = (Corrector) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(identificadorUsuario, that.identificadorUsuario) &&
               Objects.equals(telefono, that.telefono) &&
               Objects.equals(maximasCorrecciones, that.maximasCorrecciones) &&
               Objects.equals(materias, that.materias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identificadorUsuario, telefono, maximasCorrecciones, materias);
    }

    @Override
    public String toString() {
        return "Corrector{" +
               "id=" + id +
               ", identificadorUsuario=" + identificadorUsuario +
               ", telefono='" + telefono + '\'' +
               ", maximasCorrecciones=" + maximasCorrecciones +
               ", materias=" + materias +
               '}';
    }
}
