package es.uma.informatica.daw.corrector.controladores;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.uma.informatica.daw.corrector.dtos.CorrectorNuevo;
import es.uma.informatica.daw.corrector.dtos.CorrectorResponse;
import es.uma.informatica.daw.corrector.servicios.CorrectorServicio;

@RestController
@RequestMapping("/correctores")
public class CorrectorControlador {

    @Autowired
    private CorrectorServicio correctorServicio;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VICERRECTORADO')")
    public ResponseEntity<List<CorrectorResponse>> obtenerTodosCorrectores(@RequestParam(required = false) Long idConvocatoria) {
        return ResponseEntity.ok(correctorServicio.obtenerTodosCorrectores(idConvocatoria));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'VICERRECTORADO') or @securityServicio.esPropietario(#id, authentication.name)")
    public ResponseEntity<CorrectorResponse> obtenerCorrectorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(correctorServicio.obtenerCorrectorPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<CorrectorResponse> aniadirCorrector(@Valid @RequestBody CorrectorNuevo correctorNuevo) {
        CorrectorResponse creado = correctorServicio.aniadirCorrector(correctorNuevo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarCorrector(@PathVariable Long id) {
        correctorServicio.eliminarCorrector(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<CorrectorResponse> modificarCorrector(@PathVariable Long id, @Valid @RequestBody CorrectorNuevo correctorNuevo) {
        return ResponseEntity.ok(correctorServicio.modificarCorrector(id, correctorNuevo));
    }
}
