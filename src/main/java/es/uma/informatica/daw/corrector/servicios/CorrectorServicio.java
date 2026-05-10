package es.uma.informatica.daw.corrector.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import es.uma.informatica.daw.corrector.excepciones.CorrectorNoEncontrado;
import es.uma.informatica.daw.corrector.models.Corrector;
import es.uma.informatica.daw.corrector.repositories.CorrectorRepository;

@Service
public class CorrectorServicio {

    private CorrectorRepository correctorRepository;
    public CorrectorServicio(CorrectorRepository correctorRepository) {
        this.correctorRepository = correctorRepository  ;
    }

    public List<Corrector> obtenerTodosCorrectores() {
        return correctorRepository.findAll();
    }

    public Corrector obtenerCorrectorPorId(Long id) {
        return correctorRepository.findById(id)
            .orElseThrow(() -> new CorrectorNoEncontrado());
    }

    public List<Corrector> obtenerCorrectoresPorNombre(String nombre) {
        return correctorRepository.findByNombre(nombre);
    }

    public Corrector aniadirCorrector(Corrector corrector) {
        corrector.setId(null);
        return correctorRepository.save(corrector);
    }
    public void eliminarCorrector(Long id) {
        Corrector corrector = obtenerCorrectorPorId(id);
        correctorRepository.deleteById(id);
    }
    public Corrector modificarCorrector(Long id, Corrector corrector) {
        Corrector existente = obtenerCorrectorPorId(id);
        existente.setMaximasCorrecciones(corrector.getMaximasCorrecciones());
        existente.setTelefono(corrector.getTelefono());
        existente.setMaterias(corrector.getMaterias());
        correctorRepository.save(existente);
        return existente;

    }
}
}
