package com.sistema.bitacora.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler{

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    404 - recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound
        (ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body((e.getMessage()));
    }
//    404 - recurso no encontrado (Optional.get() / service que lanza NoSuchElementException)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElement(NoSuchElementException e){
        String msg = (e.getMessage()!=null)
                ? e.getMessage()
                : "Recurso no encontrado";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }
//    400 - Operación inválida (ej. nombre duplicado)
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<?> handleInvalidOperation(InvalidOperationException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
//    400 - ResponseStatusException lanzada desde el servicio (ej. activo=null)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatus(ResponseStatusException e){
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
//    400 Valor que no se puede convertir al tipo esperado
//    ( caso 5: activo=xyz no es Boolean; caso 6 id=abc no es Long)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e){
        String valor = String.valueOf(e.getValue());
        String tipo = (e.getRequiredType()!=null)
                ? e.getRequiredType().getSimpleName()
                : "desconocido";
        return ResponseEntity.badRequest()
                .body("Valor invalido '"+valor+"' para el parámetro '"+e.getName()+
                        "'.Se esperaba un "+tipo+".");
    }
//    500 - fallback genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception e){
//                Conviene loguear el error real
        log.error("Error inesperado no controlado",e);
        return ResponseEntity.internalServerError()
                .body("Ha ocurrido un error inesperado");

    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException e){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Método "+e.getMethod()+" no permitido para la ruta. "+
                        "Métodos soportados: "+e.getSupportedHttpMethods());
    }
//    404 - ruta no encontrada (SpringBoot 6.1 + Boot 3.2)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFound(NoResourceFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Ruta no encontrada: "+e.getResourcePath());
    }

//    Ruta no encontrada (Spring Boot con trhow-exception-if-no-handler-found-true
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFound(NoHandlerFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Ruta no encontrada: "+e.getRequestURL());
    }

//    400 - Validación de @Valid falló en el @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e){
        Map<String,String > errores =e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage()!=null
                            ? fe.getDefaultMessage()
                            :"valor invalido",
                        (a,b)->a // si hay dos errores en el mismo campo quedate con el primero
                ));
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleNotReadable(HttpMessageNotReadableException e){
//        Case enum inválido: extraemos el valor y los valores válidos del mensaje
        Throwable cause= e.getCause();
        if(cause instanceof InvalidFormatException ife
            && ife.getTargetType()!=null
            && ife.getTargetType().isEnum()){

            String valor = String.valueOf(ife.getValue());
            String campo = ife.getPath().isEmpty()
                    ?"desconocido"
                    : ife.getPath().get(ife.getPath().size() -1).getPropertyName();
            String validos = java.util.Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(java.util.stream.Collectors.joining(", "));

            return ResponseEntity.badRequest().body(
                    "Valor inválido '"+valor+"' para el campo '"+campo+
                            "'.Valores permitidos "+validos);
        }
//        cualquier otro JSon Mal formado
        return ResponseEntity.badRequest()
                .body("JSON malformado o tipo invalido en el body");
    }

//    400 - falta un @RequestParam obligatorio
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException e){
        return ResponseEntity.badRequest()
                .body("Falta el parametro requerido: "+e.getParameterName());
    }
}
