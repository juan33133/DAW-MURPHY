package es.uma.informatica.daw.corrector.servicios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uma.informatica.daw.corrector.dtos.CorrectorNuevo;
import es.uma.informatica.daw.corrector.dtos.CorrectorResponse;
import es.uma.informatica.daw.corrector.excepciones.CorrectorNoEncontradoException;
import es.uma.informatica.daw.corrector.excepciones.UsuarioYaExisteException;
import es.uma.informatica.daw.corrector.mappers.CorrectorMapper;
import es.uma.informatica.daw.corrector.models.Corrector;
import es.uma.informatica.daw.corrector.repositories.CorrectorRepository;

@Service
public class CorrectorServicio {

    @Autowired
    private CorrectorRepository correctorRepository;

    @Autowired
    private ClienteMicroservicios clienteMicroservicios;

    // GET /correctores
    @Transactional(readOnly = true)
    public List<CorrectorResponse> obtenerTodosCorrectores(Long idConvocatoria) {
        List<Corrector> list = (idConvocatoria == null)
            ? correctorRepository.findAll()
            : correctorRepository.findByMateriasIdConvocatoria(idConvocatoria);

        return list.stream()
                .map(CorrectorMapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET /correctores/{id}
    @Transactional(readOnly = true)
    public CorrectorResponse obtenerCorrectorPorId(Long id) {
        return correctorRepository.findById(id)
                .map(CorrectorMapper::toDTO)
                .orElseThrow(() -> new CorrectorNoEncontradoException("ERR::GET::ID inexistente."));
    }

    // POST /correctores
    @Transactional
    public CorrectorResponse aniadirCorrector(CorrectorNuevo correctorNuevo) {
        if (!clienteMicroservicios.existeUsuario(correctorNuevo.getIdentificadorUsuario())) {
            throw new IllegalArgumentException("ERR::POST::El usuario no existe.");
        }
        if (!clienteMicroservicios.existeMateria(correctorNuevo.getMateria().getId())) {
            throw new IllegalArgumentException("ERR::POST::La materia no existe.");
        }
        if (!clienteMicroservicios.isConvocatoriaVigente(correctorNuevo.getIdentificadorConvocatoria())) {
            throw new IllegalArgumentException("ERR::POST::La convocatoria no se encuentra vigente.");
        }

        if (correctorRepository.existsByIdentificadorUsuario(correctorNuevo.getIdentificadorUsuario())) {
            throw new UsuarioYaExisteException("ERR::POST::Hay un conflicto con el identificador de usuario.");
        }

        Corrector corrector = CorrectorMapper.toEntity(correctorNuevo);
        corrector.getMaterias().add(CorrectorMapper.toMateriaRelacion(correctorNuevo, corrector));

        return CorrectorMapper.toDTO(correctorRepository.save(corrector));
    }

    // DELETE /correctores/{id}
    @Transactional
    public void eliminarCorrector(Long id) {
        if (!correctorRepository.existsById(id)) {
            throw new CorrectorNoEncontradoException("ERR::DELETE::ID inexistente.");
        }
        correctorRepository.deleteById(id);
    }

    // PUT /correctores/{id}
    @Transactional
    public CorrectorResponse modificarCorrector(Long id, CorrectorNuevo correctorNuevo) {
        Corrector corrector = correctorRepository.findById(id)
                .orElseThrow(() -> new CorrectorNoEncontradoException("ERR::PUT::ID inexistente."));

        if (!clienteMicroservicios.existeMateria(correctorNuevo.getMateria().getId())) {
            throw new IllegalArgumentException("ERR::PUT::La materia no existe.");
        }
        if (!clienteMicroservicios.isConvocatoriaVigente(correctorNuevo.getIdentificadorConvocatoria())) {
            throw new IllegalArgumentException("ERR::PUT::La convocatoria no se encuentra vigente.");
        }

        boolean existeMateriaEnConvocatoria = corrector.getMaterias().stream()
                .anyMatch(m -> m.getIdConvocatoria().equals(correctorNuevo.getIdentificadorConvocatoria()) && m.getIdMateria().equals(correctorNuevo.getMateria().getId()));
        if (!existeMateriaEnConvocatoria) {
            corrector.getMaterias().add(CorrectorMapper.toMateriaRelacion(correctorNuevo, corrector));
        }

        corrector.setTelefono(correctorNuevo.getTelefono());
        corrector.setMaximasCorrecciones(correctorNuevo.getMaximasCorrecciones());

        return CorrectorMapper.toDTO(correctorRepository.save(corrector));
    }
}
