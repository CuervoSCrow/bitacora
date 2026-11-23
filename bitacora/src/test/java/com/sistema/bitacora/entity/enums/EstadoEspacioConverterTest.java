package com.sistema.bitacora.entity.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
    private final  EstadoEspacioConverter converter = new EstadoEspacioConverter();


    // =====================================================================
    // convertToDatabaseColumn: EstadoEspacio → String
    // =====================================================================

    @Nested
    @DisplayName("convertToDataBaseColumn(EstadoEspacio")
    class ConvertToDatabaseColumn{
        @Test
        @DisplayName("DISPONIBLE debe persistirse como \"disponible\"")
        void shouldPersistDisponibleAsLowercase(){
//            ACT
            String persistido = converter.convertToDatabaseColumn(EstadoEspacio.DISPONIBLE);

//            ASSERT
            assertThat(persistido).isEqualTo("disponible");
        }
    }
}
