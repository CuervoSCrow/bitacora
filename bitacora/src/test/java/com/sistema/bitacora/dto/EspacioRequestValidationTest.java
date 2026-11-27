package com.sistema.bitacora.dto;

import com.sistema.bitacora.dto.espacio.EspacioRequest;
import com.sistema.bitacora.entity.Espacio;
import com.sistema.bitacora.entity.enums.EstadoEspacio;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias de las restricciones Bean Validation declaradas en
 * {@link EspacioRequest}.
 *
 * <p>Estrategia:
 * <ul>
 *   <li><b>Test unitario puro</b>: NO levanta Spring, NO usa base de datos.
 *       Solo instancia un {@link Validator} de Jakarta Bean Validation.</li>
 *   <li><b>Milisegundos</b> por test: ideal para correr en cada commit.</li>
 *   <li>Verifica <b>mensajes literales</b>: los mensajes son contrato con el
 *       cliente (el frontend los muestra al usuario).</li>
 *   <li><b>Casos borde</b>: null, vacío, solo espacios, mínimo, máximo,
 *       mínimo-1, máximo+1.</li>
 * </ul>
 *
 * <p>El {@code ValidatorFactory} se crea una sola vez en {@link BeforeAll}
 * y se cierra en {@link AfterAll} para no filtrar recursos entre tests.
 */
@DisplayName("EspacioRequest - validaciones Bean Validation")
class EspacioRequestValidationTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator=validatorFactory.getValidator();
    }
    @AfterAll
    static void closeValidatorFactory(){
        if(validatorFactory != null){
            validatorFactory.close();
        }
    }
    // =====================================================================
    // Helpers
    // =====================================================================

    /**
     * Builder con valores por defecto VÁLIDOS. Cada test sobrescribe solo
     * el campo que quiere invalidar. Así el test es explícito sobre qué
     * regla está probando.
     */
    private static EspacioRequest.EspacioRequestBuilder validRequestBuilder(){
        return EspacioRequest.builder()
                .nombreEspacio("Sala Principal")
                .ubicacion("Piso 1")
                .capacidad(20)
                .estadoActual(EstadoEspacio.DISPONIBLE)
                .activo(true);
    }

    private static Set<String> violationMessages(EspacioRequest request){
        return validator.validate(request).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }
    private static Set<String> violatedProperties(EspacioRequest request){
        return validator.validate(request).stream()
                .map((v->v.getPropertyPath().toString()))
                .collect(Collectors.toSet());
    }
    // =====================================================================
    // Caso feliz
    // =====================================================================
    @Test
    @DisplayName("Un request valido no debe generar violaciones")
    void shouldHaveNoViolations_whenRequestIsValid(){
//        Act
        Set<ConstraintViolation<EspacioRequest>>violations =
                validator.validate(validRequestBuilder().build());
//        Assert
        assertThat(violations)
                .as("Un request con todos los campos válidos no debe violar ninguna regla")
                .isEmpty();
    }

    // =====================================================================
    // nombreEspacio
    // =====================================================================
    @Nested
    @DisplayName("nombreEspacio")
    class nombreEspacioValidation{
        @Test
        @DisplayName("Debe fallar cuando es nulo")
        void showFail_WhenNull(){
//            Arrange
            EspacioRequest request = validRequestBuilder()
                    .nombreEspacio(null)
                    .build();
//            Act + Arrange
            assertThat(violationMessages(request))
                    .containsExactly("El nombre del espacio es obligatorio");

        }

        @Test
        @DisplayName("Debe fallar cuando este vacio")
        void shouldFail_whenEmpty(){
//            Arrange
            EspacioRequest request = validRequestBuilder()
                    .nombreEspacio("")
                    .build();
//            Act + Assert
            assertThat(violationMessages(request))
                    .containsExactly("El nombre del espacio debe ser obligatorio");
        }
    }

}
