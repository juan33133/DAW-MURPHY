package es.uma.informatica.daw.corrector.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.uma.informatica.daw.corrector.dtos.CorrectorNuevo;
import es.uma.informatica.daw.corrector.dtos.CorrectorResponse;
import es.uma.informatica.daw.corrector.servicios.CorrectorServicio;

@RestController
@RequestMapping("/correctores")
public class CorrectorControlador {

    @Autowired
    private CorrectorServicio correctorServicio;

    @GetMapping
    public ResponseEntity<List<CorrectorResponse>> obtenerTodosCorrectores(@RequestParam(required = false) Long idConvocatoria) {
        return ResponseEntity.ok(correctorServicio.obtenerTodosCorrectores(idConvocatoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorrectorResponse> obtenerCorrectorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(correctorServicio.obtenerCorrectorPorId(id));
    }

    @PostMapping
    public ResponseEntity<CorrectorResponse> aniadirCorrector(@RequestBody CorrectorNuevo correctorNuevo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(correctorServicio.aniadirCorrector(correctorNuevo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCorrector(@PathVariable Long id) {
        correctorServicio.eliminarCorrector(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorrectorResponse> modificarCorrector(@PathVariable Long id, @RequestBody CorrectorNuevo correctorNuevo) {
        return ResponseEntity.ok(correctorServicio.modificarCorrector(id, correctorNuevo));
    }
}
