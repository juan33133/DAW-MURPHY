package es.uma.informatica.daw.corrector.controladores;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.logging.log4j.Logger;

import es.uma.informatica.daw.corrector.dtos.CorrectorNuevo;
import es.uma.informatica.daw.corrector.dtos.DtoAndEntityMapper;
import es.uma.informatica.daw.corrector.excepciones.CorrectorNoEncontrado;
import es.uma.informatica.daw.corrector.models.Corrector;
import es.uma.informatica.daw.corrector.models.Materia;
import es.uma.informatica.daw.corrector.models.MateriaEnConvocatoria;
import es.uma.informatica.daw.corrector.servicios.CorrectorServicio;

@RestController
@RequestMapping("/correctores")
public class CorrectorControlador {
    private CorrectorServicio servicio;
    private RestClient calendarioClient;
    private Logger logger = org.apache.logging.log4j.LogManager.getLogger(CorrectorControlador.class);

    public CorrectorControlador(CorrectorServicio servicio,
            @Value("${calendario.base-url}") String calendarioBaseUrl) {
        this.servicio = servicio;
        this.calendarioClient = RestClient.builder()
                .baseUrl(calendarioBaseUrl)
                .build();
    }

    /**
     * Obtiene una materia del microservicio externo de calendario/materias.
     */
    private Materia obtenerMateriaExterna(Long idMateria) {
        try {
            return calendarioClient.get()
                    .uri("/materias/{idMateria}", idMateria)
                    .retrieve()
                    .body(Materia.class);
        } catch (Exception e) {
            logger.warn("No se pudo obtener la materia con id {} del servicio externo: {}", idMateria, e.getMessage());
            return null;
        }
    }

    private CorrectorNuevo convertirADto(Corrector corrector) {
        // Un corrector puede tener varias materias en convocatoria
        // Tomamos la primera para el DTO
        MateriaEnConvocatoria mec = corrector.getMaterias().stream().findFirst().orElse(null);
        Materia materia = null;
        Long idConvocatoria = null;
        if (mec != null) {
            idConvocatoria = mec.getIdConvocatoria();
            materia = obtenerMateriaExterna(mec.getIdMateria());
        }
        return DtoAndEntityMapper.toDto(corrector, materia, idConvocatoria);
    }

    @GetMapping("")
    public List<Corrector> obtenerTodosCorrectores(
            @RequestParam(required = false) Long idConvocatoria) {
        if (idConvocatoria != null) {
            return servicio.obtenerCorrectoresPorConvocatoria(idConvocatoria);
        }
        return servicio.obtenerTodosCorrectores();
    }

    @PostMapping("")
    public ResponseEntity<?> aniadirCorrector(@RequestBody CorrectorNuevo corrector,
            UriComponentsBuilder uriBuilder) {
        // Comprobar si ya existe un corrector con el mismo identificador de usuario
        if (corrector.getIdentificadorUsuario() != null
                && servicio.existePorIdentificadorUsuario(corrector.getIdentificadorUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Validar materia, convocatoria y su asociación
        if (corrector.getMateria() != null && corrector.getMateria().getId() != null
                && corrector.getIdentificadorConvocatoria() != null) {
            // Verificar que la materia existe
            Materia materia = obtenerMateriaExterna(corrector.getMateria().getId());
            if (materia == null) {
                return ResponseEntity.badRequest().build();
            }
        }

        Corrector entidad = DtoAndEntityMapper.toEntity(corrector);
        Corrector aniadido = servicio.aniadirCorrector(entidad);
        URI location = uriBuilder.path("/correctores/{id}")
                .buildAndExpand(aniadido.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public Corrector obtenerUnCorrector(@PathVariable Long id) {
        logger.info("Se solicita el corrector con id: {}", id);
        return servicio.obtenerCorrectorPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarCorrector(@PathVariable Long id) {
        servicio.eliminarCorrector(id);
    }

    @PutMapping("/{id}")
    public CorrectorNuevo modificarCorrector(@PathVariable Long id,
            @RequestBody CorrectorNuevo corrector) {
        Corrector entidad = DtoAndEntityMapper.toEntity(corrector);
        entidad = servicio.modificarCorrector(id, entidad);
        return convertirADto(entidad);
    }

    @ExceptionHandler(CorrectorNoEncontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noEncontrado() {
    }
}
