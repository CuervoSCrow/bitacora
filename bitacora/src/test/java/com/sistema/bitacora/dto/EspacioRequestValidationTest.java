package com.sistema.bitacora.dto;

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
public class EspacioRequestValidationTest {
}
