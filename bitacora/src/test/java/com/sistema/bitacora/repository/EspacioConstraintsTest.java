package com.sistema.bitacora.repository;

import com.sistema.bitacora.entity.Espacio;
import com.sistema.bitacora.entity.enums.EstadoEspacio;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static com.sistema.bitacora.support.EspacioTestDataBuilder.anEspacio;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas de las restricciones a nivel de base de datos de la entidad {@link Espacio}.
 *
 * <p>Verifica que las anotaciones {@code @Column} (unique, nullable, length)
 * realmente se traduzcan en constraints en el esquema generado por Hibernate
 * y que la BD rechace los datos que las violen.
 *
 * <p>Estrategia:
 * <ul>
 *   <li>{@code save()} + {@code entityManager.flush()} dentro de
 *       {@code assertThatThrownBy}: el {@code flush} fuerza el INSERT y la BD
 *       evalúa la constraint.</li>
 *   <li>NO se usa {@code clear()}: no nos interesa el round-trip, solo que el
 *       INSERT falle.</li>
 *   <li>Excepción esperada: {@link DataIntegrityViolationException} (Spring la
 *       traduce desde la SQLException de H2).</li>
 * </ul>
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Espacio - constraints de base de datos")
public class EspacioConstraintsTest {

    @Autowired
    private EspacioRepository espacioRepository;

    @Autowired
    private EntityManager entityManager;

    // =====================================================================
    // Constraint UNIQUE en nombre_espacio
    // =====================================================================

    @Nested
    @DisplayName("Constraints UNIQUE en nombre_espacio")
    class UniqueNombreEspacio {
        @Test
        @DisplayName("Debe permitir guardar un nombre único")
        void shouldAllowInsert_wheNombreIsUnique() {
//            Arrange
            Espacio espacio = anEspacio()
                    .withNombreEspacio("Sala Unica")
                    .withUbicacion("Piso 1")
                    .withCapacidad(20)
                    .withEstadoActual(EstadoEspacio.DISPONIBLE)
                    .withActivo(true)
                    .build();

//            Act
            Espacio guardado = espacioRepository.save(espacio);
            entityManager.flush();
//            Assert
            assertThat(guardado.getId())
                    .as("El espacio debe tener  ID asignado tras el flush")
                    .isNotNull();
        }

        @Test
        @DisplayName("Debe rechazar 2 espacios con el mismo nombre")
        void shouldRejectDuplicatedNombre() {
//            Arrange - primer espacio
            espacioRepository.save(anEspacio()
                    .withNombreEspacio("Sala Duplicada")
                    .withUbicacion("Piso 1")
                    .withCapacidad(20)
                    .build());
            entityManager.flush();

//            Arrange - segundo espacio con el mismo nombre
            Espacio duplicado = anEspacio()
                    .withNombreEspacio("Sala Duplicada")
                    .withUbicacion("Piso 2")
                    .withCapacidad(30)
                    .build();
//          Act + Assert
            assertThatThrownBy(() -> {
                espacioRepository.save(duplicado);
                entityManager.flush(); // Fuerza el INSERT; la BD lo rechaza
            })
                    .as("La constarint UNIQUE debe impedir nombre duplicados")
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

    }

    // =====================================================================
    // Constraints NOT NULL
    // =====================================================================

    @Nested
    @DisplayName("Constraints NOT NULL")
    class NotNullConstrints {
        @Test
        @DisplayName("Debe rechazar nombreEspacio nulo")
        void shouldRejectNullNombreEspacio() {
//           Arrange
            Espacio espacio = anEspacio()
                    .withNombreEspacio(null)
                    .withUbicacion("Piso 1")
                    .withCapacidad(20)
                    .build();
//           Act + Assert
            assertThatThrownBy(() -> {
                espacioRepository.save(espacio);
                entityManager.flush();
            }).isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @DisplayName("Debe rechazar la ubicacion nula")
        void shouldRejectNullUbicacion() {
//            Arrange
            Espacio espacio = anEspacio()
                    .withNombreEspacio("Sala sin Ubicacion")
                    .withUbicacion(null)
                    .withCapacidad(20)
                    .build();
//            Act + Assert
            assertThatThrownBy(() -> {
                espacioRepository.save(espacio);
                entityManager.flush();
            }).isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    // =====================================================================
    // Longitud máxima de columnas (length)
    // =====================================================================

    @Nested
    @DisplayName("Longitud maxima de columnas")
    class ColumnLength {
        @Test
        @DisplayName("nombreEspacio acepta hasta 100 caracteres")
        void shouldAcceptNombreUpTo100Chars() {
//            Arrange
            String nombreLimite = "A".repeat(100);
//            Act
            Espacio guardado = espacioRepository.save(anEspacio()
                    .withNombreEspacio(nombreLimite)
                    .withUbicacion("Piso 1")
                    .withCapacidad(20)
                    .build());
            entityManager.flush();
//            Assert
            assertThat(guardado.getId()).isNotNull();
        }

        @Test
        @DisplayName("nombreEspacio rechaza mas de 100 caracteres")
        void shouldRejectNombreOver100Chars(){
//            Arrange
            String nombreExcedido = "A".repeat(101);
//            Act+Assert
            assertThatThrownBy(()->{
                espacioRepository.save(anEspacio()
                        .withNombreEspacio(nombreExcedido)
                        .withUbicacion("Piso 1")
                        .withCapacidad(20)
                        .build());
                entityManager.flush();
            }).isInstanceOf(DataIntegrityViolationException.class);

        }

        @Test
        @DisplayName("Ubicación rechaza más de 150 caracteres")
        void shouldRejectUbicacionOver150chars(){
//            Arrange
            String ubicacionExtendida = "B".repeat(151);
//            Act + Arrange
            assertThatThrownBy(()->{
                espacioRepository.save(anEspacio()
                        .withNombreEspacio("Sala Ubicación Larga")
                        .withUbicacion(ubicacionExtendida)
                        .withCapacidad(20)
                        .build());
                entityManager.flush();
            }).isInstanceOf(DataIntegrityViolationException.class);
        }
    }



}
