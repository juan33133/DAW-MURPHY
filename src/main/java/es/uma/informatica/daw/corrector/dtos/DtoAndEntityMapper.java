package es.uma.informatica.daw.corrector.dtos;

import java.util.HashSet;

import es.uma.informatica.daw.corrector.models.Corrector;
import es.uma.informatica.daw.corrector.models.Materia;
import es.uma.informatica.daw.corrector.models.MateriaEnConvocatoria;

public class DtoAndEntityMapper {

    public static CorrectorNuevo toDto(Corrector corrector, Materia materia, Long idConvocatoria) {
        if (corrector == null) {
            return null;
        }
        return new CorrectorNuevo(
                corrector.getIdentificadorUsuario(),
                idConvocatoria,
                corrector.getTelefono(),
                materia,
                corrector.getMaximasCorrecciones());
    }

    public static Corrector toEntity(CorrectorNuevo correctorNuevo) {
        if (correctorNuevo == null) {
            return null;
        }
        Corrector corrector = new Corrector(
                null,
                correctorNuevo.getIdentificadorUsuario(),
                correctorNuevo.getTelefono(),
                correctorNuevo.getMaximasCorrecciones(),
                new HashSet<MateriaEnConvocatoria>());

        if (correctorNuevo.getMateria() != null && correctorNuevo.getIdentificadorConvocatoria() != null) {
            MateriaEnConvocatoria mec = new MateriaEnConvocatoria();
            mec.setIdMateria(correctorNuevo.getMateria().getId());
            mec.setIdConvocatoria(correctorNuevo.getIdentificadorConvocatoria());
            corrector.getMaterias().add(mec);
        }

        return corrector;
    }
}
