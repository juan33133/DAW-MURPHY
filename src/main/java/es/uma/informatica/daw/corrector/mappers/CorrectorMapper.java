package es.uma.informatica.daw.corrector.mappers;

import java.util.stream.Collectors;

import es.uma.informatica.daw.corrector.dtos.CorrectorNuevo;
import es.uma.informatica.daw.corrector.dtos.CorrectorResponse;
import es.uma.informatica.daw.corrector.dtos.MateriaResponse;
import es.uma.informatica.daw.corrector.models.Corrector;
import es.uma.informatica.daw.corrector.models.MateriaEnConvocatoria;

public class CorrectorMapper {
    public static Corrector toEntity(CorrectorNuevo correctorNuevo) {
        if (correctorNuevo == null) {
            return null;
        }

        return new Corrector(
                correctorNuevo.getIdentificadorUsuario(),
                correctorNuevo.getTelefono(),
                correctorNuevo.getMaximasCorrecciones()
            );
    }

    public static MateriaEnConvocatoria toMateriaRelacion(CorrectorNuevo correctorNuevo, Corrector corrector) {
        if (correctorNuevo == null || correctorNuevo.getMateria() == null) return null;

        return new MateriaEnConvocatoria(
                correctorNuevo.getMateria().getId(),
                correctorNuevo.getIdentificadorConvocatoria(),
                corrector
            );
    }

    public static CorrectorResponse toDTO(Corrector corrector) {
        return new CorrectorResponse(
                corrector.getId(),
                corrector.getIdentificadorUsuario(),
                corrector.getTelefono(),
                corrector.getMaximasCorrecciones(),
                corrector.getMaterias().stream().map(m -> {
                    return new MateriaResponse(
                            m.getIdMateria(),
                            m.getIdConvocatoria()
                        );
                }).collect(Collectors.toList())
            );
    }
}
