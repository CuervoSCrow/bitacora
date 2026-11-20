package com.sistema.bitacora.repository;

import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.entity.Espacio;
import com.sistema.bitacora.entity.enums.EstadoEspacio;
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
    class findEspacioByNombre{
        @Test
        @DisplayName("Debe retornar el dto cuando el nombre coincida exactamente")
        void shouldReturnDto_whenNombreMatchesExactly(){
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
        void shouldReturnEmpty_whenNombreDoesNotExists(){
//            Act
            Optional<EspacioResponse> resultado = espacioRepository.findEspacioByNombre("Sala Inexistente");

//            Assert
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("Debe ser case-sensitive por que el JPQL usa '='")
        void shouldBeCaseSensitive_whenComparingName(){
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
    class findAllEspacio{
        @Test
        @DisplayName("Debe retornar todos los espacios mapeados a DTO")
        void shouldReturnAllEspaciosMappedToDto(){
//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findAllEspacio(Sort.unsorted());

//            Assert
            assertThat(resultado)
                    .hasSize(4)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactlyInAnyOrder(
                            "Sala A","Sala B","Laboratorio C","Auditorio"
                    );
        }

        @Test
        @DisplayName("Debe respetar el orden indicado por Sort")
        void shouldSortResults_whenSortIsProvided(){
//            Arrange
            Sort sortByNombreAsc = Sort.by(Sort.Direction.ASC,"nombreEspacio");

//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findAllEspacio(sortByNombreAsc);
//            Assert
            assertThat(resultado)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactly("Auditorio","Laboratorio C","Sala A","Sala B");
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay registros")
        void shouldReturnEmptyList_whenNoRecordExists(){
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
    class FindByNombreContainingIgnoreCase{
        @Test
        @DisplayName("debe retornar coincidencias parciales sin distinyir mayusculas y minusculas")
        void shouldReurnPartialMatchesIgnoringCase(){
//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findByNombreContainigIgnoreCase("Sala",Sort.unsorted());

//            Assert
            assertThat(resultado)
                    .hasSize(2)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactlyInAnyOrder("Sala A","Sala B");
        }

        @Test
    @DisplayName("Debe ignorar mayúsculas/minúsculas en el infix")
    void shouldIgnoreCase_WhenInfixUppercase(){
//            Act
            List<EspacioResponse> resultado =
                    espacioRepository.findByNombreContainigIgnoreCase("LABORATORIO",Sort.unsorted());

//            Assert
            assertThat(resultado)
                    .hasSize(1)
                    .extracting(EspacioResponse::getNombreEspacio)
                    .containsExactly("Laboratorio C");
        }
    }

    @Test
    @DisplayName("Debe Retornar Vacio cuando no hay coincidencias")
    void shouldRetirnEmpty_whenNotMatches(){
//        Act
        List<EspacioResponse> resultado =
                espacioRepository.findByNombreContainigIgnoreCase("xyz",Sort.unsorted());
//        Asset
        assertThat(resultado).isEmpty();

    }


}
