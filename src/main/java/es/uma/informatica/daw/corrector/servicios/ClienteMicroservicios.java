package es.uma.informatica.daw.corrector.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import es.uma.informatica.daw.corrector.dtos.ConvocatoriaDTO;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ClienteMicroservicios {

    /* ====== EJECUCION LOCAL ====== */
    // public boolean existeUsuario(Long idUsuario) {
    //     return idUsuario != null && idUsuario > 0;
    // }

    // public boolean existeMateria(Long idMateria) {
    //     return idMateria != null && idMateria > 0;
    // }

    // public boolean isConvocatoriaVigente(Long idConvocatoria) {
    //     return idConvocatoria != null && idConvocatoria == 2;
    // }

    /* ====== EJECUCION REMOTA ====== */
    @Autowired
    private RestClient restClient;

    @Autowired
    private HttpServletRequest request;

    @Value("${endpoint.url}")
    private String baseUrl;

    private String getAuthToken() {
        return request.getHeader("Authorization");
    }

    public boolean existeUsuario(Long idUsuario) {
        try {
            restClient.get()
                .uri(baseUrl + "/usuarios/{id}", idUsuario)
                .header("Authorization", getAuthToken())
                .retrieve()
                .toBodilessEntity();

            return true;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean existeMateria(Long idMateria) {
        try {
            restClient.get()
                .uri(baseUrl + "/materias/{id}", idMateria)
                .header("Authorization", getAuthToken())
                .retrieve()
                .toBodilessEntity();

            return true;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean isConvocatoriaVigente(Long idConvocatoria) {
        try {
            ConvocatoriaDTO convocatoriaActual = restClient.get()
                .uri(baseUrl + "/convocatorias/actual")
                .header("Authorization", getAuthToken())
                .retrieve()
                .body(ConvocatoriaDTO.class);

            return convocatoriaActual != null &&
                convocatoriaActual.getIdConvocatoria().equals(idConvocatoria);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
