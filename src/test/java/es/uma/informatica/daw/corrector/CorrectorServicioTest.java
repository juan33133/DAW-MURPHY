package es.uma.informatica.daw.corrector.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.uma.informatica.daw.corrector.dtos.CorrectorNuevo;
import es.uma.informatica.daw.corrector.dtos.CorrectorResponse;
import es.uma.informatica.daw.corrector.dtos.Materia;
import es.uma.informatica.daw.corrector.excepciones.CorrectorNoEncontradoException;
import es.uma.informatica.daw.corrector.excepciones.UsuarioYaExisteException;
import es.uma.informatica.daw.corrector.models.Corrector;
import es.uma.informatica.daw.corrector.models.MateriaEnConvocatoria;
import es.uma.informatica.daw.corrector.repositories.CorrectorRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del CorrectorServicio")
class CorrectorServicioTest {

    @Mock
    private CorrectorRepository correctorRepository;

    @InjectMocks
    private CorrectorServicio correctorServicio;

    private Corrector correctorMuestra;
    private CorrectorNuevo correctorNuevoMuestra;

    @BeforeEach
    void setUp() {
        correctorMuestra = new Corrector(100L, "600123456", 50);
        correctorMuestra.setId(1L);

        correctorNuevoMuestra = new CorrectorNuevo(
            200L,
            1L,
            "951234567",
            new Materia(1L, "Programación"),
            75
        );
    }

    @Nested
    @DisplayName("obtenerTodosCorrectores")
    class ObtenerTodosCorrectores {

        @Test
        @DisplayName("Obtener todos los correctores sin filtro")
        void testObtenerTodosCorrectoresSinFiltro() {
            // Arrange
            List<Corrector> correctores = new ArrayList<>();
            Corrector c1 = new Corrector(100L, "600123456", 50);
            Corrector c2 = new Corrector(200L, "951234567", 75);
            c1.setId(1L);
            c2.setId(2L);
            correctores.add(c1);
            correctores.add(c2);

            when(correctorRepository.findAll()).thenReturn(correctores);

            // Act
            List<CorrectorResponse> resultado = correctorServicio.obtenerTodosCorrectores(null);

            // Assert
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            verify(correctorRepository, times(1)).findAll();
            verify(correctorRepository, never()).findByMateriasIdConvocatoria(anyLong());
        }

        @Test
        @DisplayName("Obtener correctores con filtro de convocatoria")
        void testObtenerTodosCorrectoresConFiltro() {
            // Arrange
            Long idConvocatoria = 10L;
            List<Corrector> correctores = new ArrayList<>();
            Corrector c1 = new Corrector(100L, "600123456", 50);
            c1.setId(1L);
            correctores.add(c1);

            when(correctorRepository.findByMateriasIdConvocatoria(idConvocatoria)).thenReturn(correctores);

            // Act
            List<CorrectorResponse> resultado = correctorServicio.obtenerTodosCorrectores(idConvocatoria);

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            verify(correctorRepository, times(1)).findByMateriasIdConvocatoria(idConvocatoria);
            verify(correctorRepository, never()).findAll();
        }

        @Test
        @DisplayName("Obtener todos los correctores retorna lista vacía")
        void testObtenerTodosCorrectoresVacio() {
            // Arrange
            when(correctorRepository.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<CorrectorResponse> resultado = correctorServicio.obtenerTodosCorrectores(null);

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    class ObtenerCorrectorPorId {

        @Test
        @DisplayName("Obtener corrector existente por ID")
        void testObtenerCorrectorPorIdExistente() {
            // Arrange
            Long id = 1L;
            when(correctorRepository.findById(id)).thenReturn(Optional.of(correctorMuestra));

            // Act
            CorrectorResponse resultado = correctorServicio.obtenerCorrectorPorId(id);

            // Assert
            assertNotNull(resultado);
            assertEquals(100L, resultado.getIdentificadorUsuario());
            verify(correctorRepository, times(1)).findById(id);
        }

        @Test
        @DisplayName("Lanzar excepción cuando corrector no existe")
        void testObtenerCorrectorPorIdNoExistente() {
            // Arrange
            Long id = 999L;
            when(correctorRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            CorrectorNoEncontradoException exception = assertThrows(
                CorrectorNoEncontradoException.class,
                () -> correctorServicio.obtenerCorrectorPorId(id)
            );
            assertEquals("ERR::GET::ID inexistente.", exception.getMessage());
            verify(correctorRepository, times(1)).findById(id);
        }
    }

    @Nested
    class AniadirCorrector {

        @Test
        @DisplayName("Añadir corrector válido correctamente")
        void testAniadirCorrectorValido() {
            // Arrange
            Corrector nuevoCorrector = new Corrector(
                correctorNuevoMuestra.getIdentificadorUsuario(),
                correctorNuevoMuestra.getTelefono(),
                correctorNuevoMuestra.getMaximasCorrecciones()
            );
            nuevoCorrector.setId(2L);

            when(correctorRepository.existsByIdentificadorUsuario(correctorNuevoMuestra.getIdentificadorUsuario()))
                .thenReturn(false);
            when(correctorRepository.save(any(Corrector.class))).thenReturn(nuevoCorrector);

            // Act
            CorrectorResponse resultado = correctorServicio.aniadirCorrector(correctorNuevoMuestra);

            // Assert
            assertNotNull(resultado);
            assertEquals(200L, resultado.getIdentificadorUsuario());
            verify(correctorRepository, times(1)).existsByIdentificadorUsuario(200L);
            verify(correctorRepository, times(1)).save(any(Corrector.class));
        }

        @Test
        @DisplayName("Lanzar excepción cuando usuario ya existe")
        void testAniadirCorrectorUsuarioExistente() {
            // Arrange
            when(correctorRepository.existsByIdentificadorUsuario(correctorNuevoMuestra.getIdentificadorUsuario()))
                .thenReturn(true);

            // Act & Assert
            UsuarioYaExisteException exception = assertThrows(
                UsuarioYaExisteException.class,
                () -> correctorServicio.aniadirCorrector(correctorNuevoMuestra)
            );
            assertEquals("ERR::POST::Hay un conflicto con el ID de usuario.", exception.getMessage());
            verify(correctorRepository, times(1)).existsByIdentificadorUsuario(200L);
            verify(correctorRepository, never()).save(any(Corrector.class));
        }

        @Test
        @DisplayName("Nuevo corrector se añade con materia")
        void testAniadirCorrectorConMateria() {
            // Arrange
            Corrector nuevoCorrector = new Corrector(200L, "951234567", 75);
            nuevoCorrector.setId(2L);
            MateriaEnConvocatoria materia = new MateriaEnConvocatoria(1L, 1L, nuevoCorrector);
            nuevoCorrector.getMaterias().add(materia);

            when(correctorRepository.existsByIdentificadorUsuario(200L)).thenReturn(false);
            when(correctorRepository.save(any(Corrector.class))).thenReturn(nuevoCorrector);

            // Act
            CorrectorResponse resultado = correctorServicio.aniadirCorrector(correctorNuevoMuestra);

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.getMaterias().size());
            verify(correctorRepository, times(1)).save(any(Corrector.class));
        }
    }

    @Nested
    class EliminarCorrector {

        @Test
        @DisplayName("Eliminar corrector existente")
        void testEliminarCorrectorExistente() {
            // Arrange
            Long id = 1L;
            when(correctorRepository.existsById(id)).thenReturn(true);

            // Act
            correctorServicio.eliminarCorrector(id);

            // Assert
            verify(correctorRepository, times(1)).existsById(id);
            verify(correctorRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Lanzar excepción al eliminar corrector inexistente")
        void testEliminarCorrectorNoExistente() {
            // Arrange
            Long id = 999L;
            when(correctorRepository.existsById(id)).thenReturn(false);

            // Act & Assert
            CorrectorNoEncontradoException exception = assertThrows(
                CorrectorNoEncontradoException.class,
                () -> correctorServicio.eliminarCorrector(id)
            );
            assertEquals("ERR::DELETE::ID inexistente.", exception.getMessage());
            verify(correctorRepository, times(1)).existsById(id);
            verify(correctorRepository, never()).deleteById(id);
        }
    }

    @Nested
    class ModificarCorrector {

        @Test
        @DisplayName("Modificar corrector existente correctamente")
        void testModificarCorrectorExistente() {
            // Arrange
            Long id = 1L;
            Corrector correctorExistente = new Corrector(100L, "600123456", 50);
            correctorExistente.setId(id);

            CorrectorNuevo actualizado = new CorrectorNuevo(
                100L,
                1L,
                "666777888",
                new Materia(1L, "Programación"),
                100
            );

            when(correctorRepository.findById(id)).thenReturn(Optional.of(correctorExistente));
            when(correctorRepository.save(any(Corrector.class))).thenReturn(correctorExistente);

            // Act
            CorrectorResponse resultado = correctorServicio.modificarCorrector(id, actualizado);

            // Assert
            assertNotNull(resultado);
            verify(correctorRepository, times(1)).findById(id);
            verify(correctorRepository, times(1)).save(any(Corrector.class));
        }

        @Test
        @DisplayName("Lanzar excepción al modificar corrector inexistente")
        void testModificarCorrectorNoExistente() {
            // Arrange
            Long id = 999L;
            when(correctorRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            CorrectorNoEncontradoException exception = assertThrows(
                CorrectorNoEncontradoException.class,
                () -> correctorServicio.modificarCorrector(id, correctorNuevoMuestra)
            );
            assertEquals("ERR::PUT::ID inexistente.", exception.getMessage());
            verify(correctorRepository, never()).save(any(Corrector.class));
        }

        @Test
        @DisplayName("Modificar corrector añade nueva convocatoria si no existe")
        void testModificarCorrectorAnadirConvocatoria() {
            // Arrange
            Long id = 1L;
            Corrector correctorExistente = new Corrector(100L, "600123456", 50);
            correctorExistente.setId(id);
            MateriaEnConvocatoria materiaExistente = new MateriaEnConvocatoria(1L, 1L, correctorExistente);
            correctorExistente.getMaterias().add(materiaExistente);

            CorrectorNuevo actualizado = new CorrectorNuevo(
                100L,
                2L,  // Diferente convocatoria
                "666777888",
                new Materia(2L, "BD"),
                100
            );

            when(correctorRepository.findById(id)).thenReturn(Optional.of(correctorExistente));
            when(correctorRepository.save(any(Corrector.class))).thenReturn(correctorExistente);

            // Act
            CorrectorResponse resultado = correctorServicio.modificarCorrector(id, actualizado);

            // Assert
            assertNotNull(resultado);
            verify(correctorRepository, times(1)).save(any(Corrector.class));
        }

        @Test
        @DisplayName("Modificar corrector no añade convocatoria si ya existe")
        void testModificarCorrectorConvocatoriaExistente() {
            // Arrange
            Long id = 1L;
            Corrector correctorExistente = new Corrector(100L, "600123456", 50);
            correctorExistente.setId(id);
            MateriaEnConvocatoria materiaExistente = new MateriaEnConvocatoria(1L, 1L, correctorExistente);
            correctorExistente.getMaterias().add(materiaExistente);

            CorrectorNuevo actualizado = new CorrectorNuevo(
                100L,
                1L,  // Misma convocatoria
                "666777888",
                new Materia(1L, "Programación"),
                100
            );

            when(correctorRepository.findById(id)).thenReturn(Optional.of(correctorExistente));
            when(correctorRepository.save(any(Corrector.class))).thenReturn(correctorExistente);

            // Act
            correctorServicio.modificarCorrector(id, actualizado);

            // Assert
            assertEquals(1, correctorExistente.getMaterias().size());
            verify(correctorRepository, times(1)).save(any(Corrector.class));
        }

        @Test
        @DisplayName("Modificar actualiza teléfono y máximas correcciones")
        void testModificarActualizaTelefonoYCorrecciones() {
            // Arrange
            Long id = 1L;
            Corrector correctorExistente = new Corrector(100L, "600123456", 50);
            correctorExistente.setId(id);

            CorrectorNuevo actualizado = new CorrectorNuevo(
                100L,
                1L,
                "999888777",
                new Materia(1L, "Programación"),
                200
            );

            when(correctorRepository.findById(id)).thenReturn(Optional.of(correctorExistente));
            when(correctorRepository.save(any(Corrector.class))).thenReturn(correctorExistente);

            // Act
            correctorServicio.modificarCorrector(id, actualizado);

            // Assert
            assertEquals("999888777", correctorExistente.getTelefono());
            assertEquals(200, correctorExistente.getMaximasCorrecciones());
            verify(correctorRepository, times(1)).save(any(Corrector.class));
        }
    }
}
