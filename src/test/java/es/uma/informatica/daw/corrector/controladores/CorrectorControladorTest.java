package es.uma.informatica.daw.corrector.controladores;

import es.uma.informatica.daw.corrector.dtos.CorrectorNuevo;
import es.uma.informatica.daw.corrector.dtos.CorrectorResponse;
import es.uma.informatica.daw.corrector.dtos.Materia;
import es.uma.informatica.daw.corrector.repositories.CorrectorRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestRestTemplate
@DisplayName("Tests del CorrectorControlador")
public class CorrectorControladorTest {

    @Autowired
    private RestClient remoteClient;

    @Autowired
    private CorrectorRepository correctorRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${endpoint.url}")
    private String baseUrl;

    private String tokenAdmin;
    private HttpHeaders headersAdmin;

    record LoginResponse(String jwt) {}

    @BeforeAll
    void setUp() {
        Map<String, String> credenciales = Map.of(
                "email", "admin@uma.es",
                "password", "admin"
        );

        LoginResponse response = remoteClient.post()
                .uri(baseUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(credenciales)
                .retrieve()
                .body(LoginResponse.class);
        
        tokenAdmin = response.jwt();
        assertNotNull(tokenAdmin, "El token obtenido no debe ser nulo");

        // Preparamos las cabeceras con el token Bearer
        headersAdmin = new HttpHeaders();
        headersAdmin.setBearerAuth(tokenAdmin);
        headersAdmin.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    void tearDown() {
        correctorRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe crear un corrector correctamente y devolver 201 Created")
    void testCrearCorrectorExito() {
        // Arrange
        CorrectorNuevo correctorNuevo = new CorrectorNuevo(
            100L, 2L, "612345678", new Materia(10L), 50);
        HttpEntity<CorrectorNuevo> request = new HttpEntity<>(correctorNuevo, headersAdmin);

        // Act
        ResponseEntity<CorrectorResponse> response = restTemplate.exchange(
                "/correctores", HttpMethod.POST, request, CorrectorResponse.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "El codigo de estado debe ser 201 CREATED");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debe ser nulo");
        assertNotNull(response.getBody().getId(), "El ID del corrector creado no debe ser nulo");
        assertEquals(100L, response.getBody().getIdentificadorUsuario(), "El identificador de usuario debe coincidir");
        
        // Verificamos el header Location
        assertTrue(response.getHeaders().containsHeader(HttpHeaders.LOCATION), "Debe incluir la cabecera Location");
    }

    @Test
    @DisplayName("Debe fallar al crear un corrector con datos invalidos y devolver 400 Bad Request")
    void testCrearCorrectorFallaValidacion() {
        // Arrange: Enviamos 0 maximasCorrecciones
        CorrectorNuevo correctorNuevo = new CorrectorNuevo(
                100L, 2L, "612345678", new Materia(10L), 0);
        HttpEntity<CorrectorNuevo> request = new HttpEntity<>(correctorNuevo, headersAdmin);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                "/correctores", HttpMethod.POST, request, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Debe devolver 400 Bad Request por fallo de validación");
        assertTrue(response.getBody().contains("ERR ->"), "El mensaje de error debe contener el formato de validacion del ManejadorGlobalExcepciones");
    }

    @Test
    @DisplayName("Debe obtener la lista de correctores devolviendo 200 OK")
    void testObtenerTodosCorrectores() {
        // Arrange
        HttpEntity<Void> request = new HttpEntity<>(headersAdmin);

        // Act
        ResponseEntity<CorrectorResponse[]> response = restTemplate.exchange(
                "/correctores", HttpMethod.GET, request, CorrectorResponse[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El codigo de estado debe ser 200 OK");
        assertNotNull(response.getBody(), "La lista de correctores no debe ser nula");
    }

    @Test
    @DisplayName("Debe obtener un corrector por ID devolviendo 200 OK")
    void testObtenerCorrectorPorId() {
        // Arrange
        CorrectorNuevo correctorNuevo = new CorrectorNuevo(
                101L, 2L, "612345678", new Materia(11L), 30);
        ResponseEntity<CorrectorResponse> responseCreacion = restTemplate.exchange(
                "/correctores", HttpMethod.POST, new HttpEntity<>(correctorNuevo, headersAdmin), CorrectorResponse.class);
        Long idCreado = responseCreacion.getBody().getId();

        HttpEntity<Void> requestGet = new HttpEntity<>(headersAdmin);

        // Act
        ResponseEntity<CorrectorResponse> responseGet = restTemplate.exchange(
                "/correctores/" + idCreado, HttpMethod.GET, requestGet, CorrectorResponse.class);

        // Assert
        assertEquals(HttpStatus.OK, responseGet.getStatusCode(), "Debe devolver 200 OK al obtener por ID");
        assertNotNull(responseGet.getBody(), "El cuerpo no debe ser nulo");
        assertEquals(idCreado, responseGet.getBody().getId(), "El ID devuelto debe coincidir con el consultado");
    }

    @Test
    @DisplayName("Debe modificar un corrector existente y devolver 200 OK")
    void testModificarCorrector() {
        // Arrange
        CorrectorNuevo original = new CorrectorNuevo(
                102L, 2L, "612345678", new Materia(12L), 20);
        ResponseEntity<CorrectorResponse> responseCreacion = restTemplate.exchange(
                "/correctores", HttpMethod.POST, new HttpEntity<>(original, headersAdmin), CorrectorResponse.class);
        Long idCreado = responseCreacion.getBody().getId();

        // Modificamos datos
        CorrectorNuevo modificado = new CorrectorNuevo(
                102L, 2L, "611111111", new Materia(12L), 40);
        HttpEntity<CorrectorNuevo> requestPut = new HttpEntity<>(modificado, headersAdmin);

        // Act
        ResponseEntity<CorrectorResponse> responsePut = restTemplate.exchange(
                "/correctores/" + idCreado, HttpMethod.PUT, requestPut, CorrectorResponse.class);

        // Assert
        assertEquals(HttpStatus.OK, responsePut.getStatusCode(), "Debe devolver 200 OK tras la modificacion");
        assertEquals("611111111", responsePut.getBody().getTelefono(), "El telefono debe haberse actualizado");
        assertEquals(40, responsePut.getBody().getMaximasCorrecciones(), "El numero de correcciones debe haberse actualizado");
    }

    @Test
    @DisplayName("Debe eliminar un corrector y devolver 200 OK")
    void testEliminarCorrector() {
        // Arrange
        CorrectorNuevo correctorNuevo = new CorrectorNuevo(
                103L, 2L, "612345678", new Materia(13L), 10);
        ResponseEntity<CorrectorResponse> responseCreacion = restTemplate.exchange(
                "/correctores", HttpMethod.POST, new HttpEntity<>(correctorNuevo, headersAdmin), CorrectorResponse.class);
        Long idCreado = responseCreacion.getBody().getId();

        HttpEntity<Void> requestDelete = new HttpEntity<>(headersAdmin);

        // Act
        ResponseEntity<Void> responseDelete = restTemplate.exchange(
                "/correctores/" + idCreado, HttpMethod.DELETE, requestDelete, Void.class);

        // Assert
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode(), "Debe devolver 200 OK al eliminar");

        // Verificamos que ya no existe
        ResponseEntity<String> responseComprobacion = restTemplate.exchange(
                "/correctores/" + idCreado, HttpMethod.GET, requestDelete, String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseComprobacion.getStatusCode(), "Debe devolver 404 NOT FOUND tras ser eliminado");
    }
}
