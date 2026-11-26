package com.sistema.bitacora.entity.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias de {@link EstadoEspacioConverter}.
 *
 * <p>Contrato verificado:
 * <ul>
 *   <li>Persistencia por <b>nombre en minúsculas</b> (p. ej. {@code "disponible"}).</li>
 *   <li>Lectura tolerante a mayúsculas/minúsculas y espacios ({@code trim}).</li>
 *   <li>{@code null} en ambos sentidos → {@code null}.</li>
 *   <li>Valores desconocidos → <b>fallback a {@link EstadoEspacio#DISPONIBLE}</b>
 *       (no lanza excepción; degradación silenciosa con log de warning).</li>
 * </ul>
 *
 * <p>Test unitario puro: no requiere Spring ni base de datos.
 */
@DisplayName("EstadoEspacioConverter - pesistencia por nombre en minusculas")
public class EstadoEspacioConverterTest {
    private final EstadoEspacioConverter converter = new EstadoEspacioConverter();
    private static String persistido;
    private static EstadoEspacio estado;

    // =====================================================================
    // convertToDatabaseColumn: EstadoEspacio → String
    // =====================================================================

    @Nested
    @DisplayName("convertToDataBaseColumn(EstadoEspacio")
    class ConvertToDatabaseColumn {

        @Test
        @DisplayName("DISPONIBLE debe persistirse como \"disponible\"")
        void shouldPersistDisponibleAsLowercase() {
//            ACT
            persistido = converter.convertToDatabaseColumn(EstadoEspacio.DISPONIBLE);

//            ASSERT
            assertThat(persistido).isEqualTo("disponible");
        }

        @Test
        @DisplayName("OCUPADO debe persistir como \'ocupado\'")
        void shouldPersistOcupadoAsLowercase() {
//            ACT
            persistido = converter.convertToDatabaseColumn(EstadoEspacio.OCUPADO);
//            ASSERT
            assertThat(persistido).isEqualTo("ocupado");
        }

        @Test
        @DisplayName("MANTENIMIENTO debe persistir como \'mantenimiento\'")
        void shouldPersistMantenimientoAsLowercase() {
            assertThat(converter.convertToDatabaseColumn(EstadoEspacio.MANTENIMIENTO))
                    .isEqualTo("mantenimiento");
        }

        @Test
        @DisplayName("INACTIVO debe persistir como \'inactivo\'")
        void shouldPersistInactivoAsLowercase() {
            assertThat(converter.convertToDatabaseColumn(EstadoEspacio.INACTIVO))
                    .isEqualTo("inactivo");
        }

        @ParameterizedTest(name = "{0} debe persistir en minusculas")
        @EnumSource(EstadoEspacio.class)
        @DisplayName("cada valor debe persistir como su nombre en minusculas")
        void shouldPersistEveryValueAsLowerCaseName(EstadoEspacio estado) {
//            ACT
            persistido = converter.convertToDatabaseColumn(estado);
//            ASSERT
            assertThat(persistido)
                    .as("Persistencia de %s", estado.name())
                    .isEqualTo(estado.name().toLowerCase());
        }

        @Test
        @DisplayName("debe retornar null cuando el enum es null")
        void shouldReturnNull_WhenEnumIsNull() {
//            ACT
            persistido = converter.convertToDatabaseColumn(null);
//            ASSERT
            assertThat(persistido).isNull();


        }
    }

    // =====================================================================
    // convertToEntityAttribute: String → EstadoEspacio
    // =====================================================================
    @Nested
    @DisplayName("convertToEntityAttribute(String)")
    class ConvertToEntityAttribute {
        @Test
        @DisplayName("debe construir desde minusculas: \"disponible\" -> DISPONIBLE")
        void shouldReconstructFromLowercase() {
//                ACT
            estado = converter.convertToEntityAttribute("disponible");
//                ASSERT
            assertThat(estado).isEqualTo(EstadoEspacio.DISPONIBLE);
        }

        @Test
        @DisplayName("debe construir desde MAYÚSCULAS: \"OCUPADO\" -> OCUPADO")
        void shouldResconstructFromUppercase() {
//                    ACT
            estado = converter.convertToEntityAttribute("OCUPADO");
//                ASSERT
            assertThat(estado).isEqualTo(EstadoEspacio.OCUPADO);

        }

        @Test
        @DisplayName("debe construir desde Mixto: \"Mantenimiento\" -> MANTENIMIENTO")
        void shouldReconstructFromMixedCase() {
//                ACT
            estado = converter.convertToEntityAttribute("Mantenimiento");
//                ASSERT
            assertThat(estado).isEqualTo(EstadoEspacio.MANTENIMIENTO);
        }

        @Test
        @DisplayName("debe ignorar espacios al principio y al final: \" inactivo \" -> INACTIVO")
        void shouldTrimWhiteespace() {
//                ACT
            estado = converter.convertToEntityAttribute(" Inactivo ");
//                ASSERT
            assertThat(estado).isEqualTo(EstadoEspacio.INACTIVO);
        }

        @ParameterizedTest(name = "\"{0}\" debe reconstruir {0}")
        @EnumSource(EstadoEspacio.class)
        @DisplayName("cada nombre en minusculas debe reconstruir su enum")
        void shouldReonstructFromEveryLowercaseName(EstadoEspacio espacio) {
//                ACT
            estado = converter.convertToEntityAttribute(espacio.name().toLowerCase());

//            ASSERT
            assertThat(estado).isEqualTo(espacio);

        }

        @Test
        @DisplayName("debe retornar null cuando el valor persistido es null")
        void shouldReturnNull_WhenDbDataIsNull() {
//                ACT
            estado = converter.convertToEntityAttribute(null);
//                ASSERT
            assertThat(estado).isNull();
        }

        // -----------------------------------------------------------------
        // Fallback ante valores desconocidos (contrato explícito del converter)
        // -----------------------------------------------------------------
        @ParameterizedTest(name = "valor desconocido \"{0}\" debe degradar a DISPONIBLE")
        @ValueSource(strings = {"basura", "DESCONOCIDO", "123", "disponible2", "OCUPADOS","null"})
        @DisplayName("valores desconocidos deben degradar a DISPONIBLE(fallback)")
        void shouldFallBacktoDisponible_whenUknownValue(String valorDesconocido) {
//                ACT
            estado = converter.convertToEntityAttribute(valorDesconocido);
//                ASSERT
            assertThat(estado)
                    .as("El converter degrada silenciosamente a DISPONIBLE; " +
                            "si cambias este contrato, actualiza el test y avisa al equipo.")
                    .isEqualTo(EstadoEspacio.DISPONIBLE);
        }

        @Test
        @DisplayName("cadena vacia debe degradar a DISPONIBLE")
        void shouldFallbackToDisponible_whenEmptyString() {
//                ACT
            estado = converter.convertToEntityAttribute("");
//                ASSERT
            assertThat(estado).isEqualTo(EstadoEspacio.DISPONIBLE);
        }

        @Test
        @DisplayName("Cadena solo con espacios debe degradar a DISPONIBLE(fallback)")
        void shouldFallbackToDisponible_whenBlackString() {
//                ACT
            estado = converter.convertToEntityAttribute("  ");
//                ASSERT
            assertThat(estado).isEqualTo(EstadoEspacio.DISPONIBLE);
        }

    }

    @Nested
    @DisplayName("Round-trip-enum ->String -> enum")
    class RoundTrip {
        @ParameterizedTest(name = "round-trip estable para {0}")
        @EnumSource(EstadoEspacio.class)
        @DisplayName("debe preservar el valor tras ida y vuelta")
        void shouldPreserveValueAfterRoundTrip(EstadoEspacio original) {
//                ACT
            persistido = converter.convertToDatabaseColumn(original);
            estado = converter.convertToEntityAttribute(persistido);
//                ASSERT
            assertThat(estado)
                    .as("El enum %s debe sobrevivir a la ida y vuelta")
                    .isEqualTo(original);
        }

        @ParameterizedTest(name = "round-trip con mayusculas para {0}")
        @EnumSource(EstadoEspacio.class)
        @DisplayName("debe tolerar mayusculas en la lectura (round - trip con name()")
        void shouldPreserveValue_whenReadingUppercaseName(EstadoEspacio original) {
//                ACT -- simulamos datos legacy que se guardan con name() en mayúsculas
            EstadoEspacio reconstruido = converter.convertToEntityAttribute(original.name());
//                ASSERT
            assertThat(reconstruido).isEqualTo(original);
        }
    }

    // =====================================================================
    // Consistencia entre los dos métodos
    // =====================================================================
    @Nested
    @DisplayName("Consistencia del contrato")
    class ConsistenciaDelContrato {
        @Test
        @DisplayName("el valor persistido nunca debe ser null para un enum nulo")
        void shouldNeverPersistNull_forNoNullEnum() {
            for (EstadoEspacio estado : EstadoEspacio.values()) {
                assertThat(converter.convertToDatabaseColumn(estado))
                        .as("Persistencia de %s", estado.name())
                        .isNotNull();
            }
        }

        @Test
        @DisplayName("el valor persistido debe estar en minusculas")
        void shouldAlwaysPersistInLowerCase() {
            for (EstadoEspacio estado : EstadoEspacio.values()) {
                persistido = converter.convertToDatabaseColumn(estado);
                assertThat(persistido)
                        .as("Persistencia de %s debe ser minúsculas", estado.name())
                        .isEqualTo(persistido.toLowerCase());
            }
        }
    }

}

