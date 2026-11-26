package com.sistema.bitacora.repository;

import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.entity.Espacio;
import com.sistema.bitacora.entity.enums.EstadoEspacio;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.sistema.bitacora.support.EspacioTestDataBuilder.anEspacio;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Pruebas de integración de la capa de persistencia para {@link EspacioRepository}.
 *
 * <p>Estrategia:
 * <ul>
 *   <li>{@code @DataJpaTest}: levanta solo el contexto JPA + H2 en memoria.</li>
 *   <li>Patrón AAA (Arrange-Act-Assert) en cada test.</li>
 *   <li>Datos semilla aislados por {@code @BeforeEach} para garantizar independencia.</li>
 *   <li>Nombres en formato {@code shouldXxx_whenYyy} para documentar comportamiento.</li>
 * </ul>
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("EspacioRepository - pruebas de persistencia")
public class EspacioRepositoryTest {
    @Autowired
    private EspacioRepository espacioRepository;
    @Autowired
    private EntityManager entityManager;

    private Espacio espacioSemilla;

    @BeforeEach
    void setup() {
        // Arrange global: dataset determinista para todos los tests
        espacioSemilla = espacioRepository.save(
                anEspacio()
                        .withNombreEspacio("Sala A")
                        .withUbicacion("Piso 1")
                        .withCapacidad(20)
                        .withEstadoActual(EstadoEspacio.DISPONIBLE)
                        .withActivo(true)
                        .build());

        espacioRepository.save(anEspacio()
                .withNombreEspacio("Sala B")
                .withUbicacion("Piso 1")
                .withCapacidad(15)
                .withEstadoActual(EstadoEspacio.OCUPADO)
                .withActivo(true)
                .build());

        espacioRepository.save(anEspacio()
                .withNombreEspacio("Laboratorio C")
                .withUbicacion("Piso 2")
                .withCapacidad(30)
                .withEstadoActual(EstadoEspacio.DISPONIBLE)
                .withActivo(false)
                .build());

        espacioRepository.save(anEspacio()
                .withNombreEspacio("Auditorio")
                .withUbicacion("Piso 3")
                .withCapacidad(100)
                .withEstadoActual(EstadoEspacio.DISPONIBLE)
                .withActivo(true)
                .build());
    }// fin setup

    //    =================================================
//          findEspacioById
//    =================================================
    @Nested
    @DisplayName("findEspacioById(Long)")
    class findEspacioById {
        @Test
        @DisplayName("Debe retornar el DTO con todos los campos cuando el ID existe")
        void shouldReturnDtoWithAllFields_WhenIdExists() {
//            Arrange
            Long idExistente = espacioSemilla.getId();
//            Act
            Optional<EspacioResponse> resultado =
                    espacioRepository.findEspacioById(idExistente);

//            Assert
            assertThat(resultado)
                    .as("Se esparaba un DTO presente para el ID %s", idExistente)
                    .isPresent()
                    .get()
                    .satisfies(dto -> {
                        assertThat(dto.getId()).isEqualTo(idExistente);
                        assertThat(dto.getNombreEspacio()).isEqualTo("Sala A");
                        assertThat(dto.getUbicacion()).isEqualTo("Piso 1");
                        assertThat(dto.getCapacidad()).isEqualTo(20);
                        assertThat(dto.getEstadoActual()).isEqualTo(EstadoEspacio.DISPONIBLE);
                        assertThat(dto.isActivo()).isTrue();
                    });
        }

        @Test
        @DisplayName("Debe retornar Optional Vacio cuando el ID no existe")
        void shouldReturnEmpty_whenIdDoesNotExists() {
//            Arrange
            Long idInexistente = 999_999L;

//            Act
            Optional<EspacioResponse> resultado =
                    espacioRepository.findEspacioById(idInexistente);

//            Assert
            assertThat(resultado)
                    .as("No debe existir un espacio con ID %s", idInexistente)
                    .isEmpty();
        }
    }

//    =================================================
//          findEspacioByNombre
//    =================================================

    @Nested
    @DisplayName("findEspacioByNombre(String")
    class findEspacioByNombre {
        @Test
        @DisplayName("Debe retornar el dto cuando el nombre coincida exactamente")
        void shouldReturnDto_whenNombreMatchesExactly() {
//            Arrange
            String nombreExistente = "Sala A";

//            Act
            Optional<EspacioResponse> resultado =
                    espacioRepository.findEspacioByNombre(nombreExistente);

//            Assert
            assertThat(resultado)
                    .isPresent()
                    .get()
                    .extracting(EspacioResponse::getNombreEspacio)
                    .isEqualTo(nombreExistente);
        }

        @Test
        @DisplayName("Debe retornar vacio cuando el nombre no existe")
        void shouldReturnEmpty_whenNombreDoesNotExists() {
//            Act
            Optional<EspacioResponse> resultado = espacioRepository.findEspacioByNombre("Sala Inexistente");

//            Assert
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("Debe ser case-sensitive por que el JPQL usa '='")
        void shouldBeCaseSensitive_whenComparingName() {
//            Act
            Optional<EspacioResponse> resultado =
                    espacioRepository.findEspacioByNombre("sala a");
//            Assert
            assertThat(resultado)
                    .as("La comparación con '=' en JPQL distingue mayúsculas")
                    .isEmpty();
        }
    }

//    =================================================
//          findAllEspacio
//    =================================================

    @Nested
    @DisplayName("findAllEspacio(Sort)")
    class findAllEspacio {
        @Test
        @DisplayName("Debe retornar todos los espacios mapeados a DTO")
        void shouldReturnAllEspaciosMappedToDto() {
//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findAllEspacio(Sort.unsorted());

//            Assert
            assertThat(resultado)
                    .hasSize(4)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactlyInAnyOrder(
                            "Sala A", "Sala B", "Laboratorio C", "Auditorio"
                    );
        }

        @Test
        @DisplayName("Debe respetar el orden indicado por Sort")
        void shouldSortResults_whenSortIsProvided() {
//            Arrange
            Sort sortByNombreAsc = Sort.by(Sort.Direction.ASC, "nombreEspacio");

//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findAllEspacio(sortByNombreAsc);
//            Assert
            assertThat(resultado)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactly("Auditorio", "Laboratorio C", "Sala A", "Sala B");
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay registros")
        void shouldReturnEmptyList_whenNoRecordExists() {
//            Arrange
            espacioRepository.deleteAll();

//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findAllEspacio(Sort.unsorted());

//            Assert
            assertThat(resultado).isEmpty();
        }
    }

    //    =================================================
//      findByNombreContainingIgnoreCase
//    =================================================

    @Nested
    @DisplayName("findByNombreContainingIgnoreCase(String, Sort)")
    class FindByNombreContainingIgnoreCase {
        @Test
        @DisplayName("debe retornar coincidencias parciales sin distinyir mayusculas y minusculas")
        void shouldReurnPartialMatchesIgnoringCase() {
//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findByNombreContainingIgnoreCase("Sala", Sort.unsorted());

//            Assert
            assertThat(resultado)
                    .hasSize(2)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactlyInAnyOrder("Sala A", "Sala B");
        }

        @Test
        @DisplayName("Debe ignorar mayúsculas/minúsculas en el infix")
        void shouldIgnoreCase_WhenInfixUppercase() {
//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findByNombreContainingIgnoreCase("LABORATORIO", Sort.unsorted());

//            Assert
            assertThat(resultado)
                    .hasSize(1)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactly("Laboratorio C");
        }

        @Test
        @DisplayName("Debe Retornar vacío cuando no hay coincidencias")
        void shouldRetirnEmpty_whenNotMatches() {
//        Act
            List<EspacioResponse> resultado =
                    espacioRepository.findByNombreContainingIgnoreCase("xyz", Sort.unsorted());
//        Asset
            assertThat(resultado).isEmpty();

        }

        @Test
        @DisplayName("debe aplicar sort sobre los resultados filtrados")
        void shouldApplySort_whenMatchesExists() {
//        Arrange
            Sort sortByNombreDesc = Sort.by(Sort.Direction.DESC, "nombreEspacio");

//        Act
            List<EspacioResponse> resultado =
                    espacioRepository.findByNombreContainingIgnoreCase("sala", sortByNombreDesc);

//        Assert
            assertThat(resultado)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactly("Sala B", "Sala A");
        }
    }

//    =================================================
//      findByActivoTrueOrderByNombreEspacioAsc
//    =================================================

    @Nested
    @DisplayName("findByActivoTrue")
    class FidByActivoTrue {
        @Test
        @DisplayName("debe retornar solo activos ordenados por nombre ascendente")
        void shouldReturnOnlyActive() {
//            Arrange
            Sort sortByNombreAsc = Sort.by(Sort.Direction.ASC, "nombreEspacio");
//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findByActivoTrue(sortByNombreAsc);

//            Assert
            assertThat(resultado)
                    .hasSize(3)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactly("Auditorio", "Sala A", "Sala B");
        }

        @Test
        @DisplayName("debe retornar vacio cuando todos los espacio esten inactivos")
        void shouldReturnEmpty_whenAllInactive() {
//            Arrange
            List<Espacio> todos = espacioRepository.findAll();
            todos.forEach(e -> e.setActivo(false));
            espacioRepository.saveAll(todos);

//            Assert
            List<EspacioResponse> resultado =
                    espacioRepository.findByActivoTrue(Sort.unsorted());

            assertThat(resultado).isEmpty();
        }
    }

//    ==========================================================
//      existsByNombreEspacioIgnoreCaseAndIdnot(String ,Long)
//    ==========================================================

    @Nested
    @DisplayName("existsByNombreEspacioIgnoreCaseAndIdNot(String, Long")
    class ExistsByNombreEspacioIgnoreCaseAndIdNot {

        @Test
        @DisplayName("debe retornar true cuando otro registro tiene el mismo nombre")
        void shouldReturnTrue_whenAnotherRecordHasSameNombre() {
//                Arrange
            Long otro_Id = 999_999L;

//                Act
            boolean existe = espacioRepository
                    .existsByNombreEspacioIgnoreCaseAndIdNot("Sala A", otro_Id);

//                Assert
            assertThat(existe).isTrue();
        }

        @Test
        @DisplayName("debe ignorar mayúsculas/minúsculas al comparar")
        void shouldIgnoreCase_whenComparingNombre() {
//                Act
            boolean existe = espacioRepository.existsByNombreEspacioIgnoreCaseAndIdNot(
                    "sAlA a", 999_999L);
//                Assert
            assertThat(existe).isTrue();
        }

        @Test
        @DisplayName("debe retornar fálse cuando el nombre pertenece al mismo ID")
        void shouldReturnFalse_whenNombreBelongToSameId() {
//                Arrange
            Long idPropio = espacioSemilla.getId();

//            Act
            boolean existe = espacioRepository
                    .existsByNombreEspacioIgnoreCaseAndIdNot(
                            "Sala A", idPropio);
//                Assert
            assertThat(existe)
                    .as("El propio registro no debe contar como duplicado")
                    .isFalse();
        }

        @Test
        @DisplayName("debe retornar false cuando el nombre existe en el sistema")
        void shouldReturnFalse_whenNombreDoesNotExists() {
//                Act
            boolean existe = espacioRepository
                    .existsByNombreEspacioIgnoreCaseAndIdNot("Nombre Nuevo", 1L);

//                Assert
            assertThat(existe).isFalse();
        }
    }

    // ==========================================================
    //   ★ Integración del EstadoEspacioConverter con JPA  ★
    // ==========================================================

    @Nested
    @DisplayName("EstadoEspacioConverter - integracion con persistencia real")
    class EstadoEspacioConverterIntegracion{
        @Test
        @DisplayName("Debe Persistir y recuperar el estadoActual via converter (round - trip a BD")
        void shouldPersistAndRetrieveEstadoActual_viaConverter(){
//            Arrange
            Espacio espacio = anEspacio()
                    .withNombreEspacio("Sala Converter")
                    .withUbicacion("Piso 7")
                    .withCapacidad(25)
                    .withEstadoActual(EstadoEspacio.MANTENIMIENTO)
                    .withActivo(true)
                    .build();
            Espacio guardado = espacioRepository.save(espacio);
//            ACT
            entityManager.flush();  // fuerza el INSERT a BD (ejecuta converter Java -> BD)
            entityManager.clear(); // vacia la cache de 1er nivel de Hibernate
            Optional<Espacio> recargado = espacioRepository.findById(guardado.getId());

//            Assert
            assertThat(recargado)
                    .as("El converter debe sobrevivir el round-trip a BD")
                    .isPresent()
                    .get()
                    .extracting(Espacio::getEstadoActual)
                    .isEqualTo(EstadoEspacio.MANTENIMIENTO);
        }

        @Test
        @DisplayName("Debe guardar el estadoActual en minusculas en la columna(SQL nativo)")
        void shouldStoreEstadoActualInLowercase_inDatabase(){
//            Arrange
            Espacio espacio = anEspacio()
                    .withNombreEspacio("Sala Minusculas")
                    .withUbicacion("Piso 8")
                    .withCapacidad(10)
                    .withEstadoActual(EstadoEspacio.OCUPADO)
                    .withActivo(true)
                    .build();
            Espacio guardado = espacioRepository.save(espacio);

//            Act - Leemos la columna cruda con sql nativo
            entityManager.flush();
            entityManager.clear();
            String valorCrudo = (String) entityManager
                    .createNativeQuery("SELECT estado_actual FROM espacios WHERE id=:id")
                    .setParameter("id",guardado.getId())
                    .getSingleResult();
//            Assert - el converter debio persistir "ocupado" no "OCUPADO"
            assertThat(valorCrudo)
                    .as("El converter persiste en minúsculas; si ves mayúsculas," +
                            "algo cambió")
                    .isEqualTo("ocupado");

        }
    }
}



