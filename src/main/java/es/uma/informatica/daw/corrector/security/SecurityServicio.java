package es.uma.informatica.daw.corrector.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.uma.informatica.daw.corrector.repositories.CorrectorRepository;

@Component("securityServicio")
public class SecurityServicio {

    @Autowired
    private CorrectorRepository correctorRepository;

    public boolean esPropietario(Long idCorrector, String authName) {
        return correctorRepository.findById(idCorrector)
            .map(c -> c.getIdentificadorUsuario().toString().equals(authName))
            .orElse(false);
    }
}
