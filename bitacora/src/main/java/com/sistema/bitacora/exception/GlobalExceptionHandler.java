package com.sistema.bitacora.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

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
}
