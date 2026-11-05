package com.sistema.bitacora.controller;


import com.sistema.bitacora.dto.espacio.EspacioRequest;
import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.exception.InvalidOperationException;
import com.sistema.bitacora.exception.ResourceNotFoundException;
import com.sistema.bitacora.service.EspacioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/espacios")
//@PreAuthorize("hasRole('ADMINISTRADOR')")
@RequiredArgsConstructor
public class EspacioController {

    private final EspacioServiceImpl espacioService;

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> findByNombre(@PathVariable String nombre) {
        Optional<EspacioResponse> espacio =
                this.espacioService.findEspacioByNombre(nombre);
        if (espacio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se ha encontrado el espacio buscado.");
        }
        return ResponseEntity.ok(espacio.get());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findBYId(@PathVariable Long id) {
        Optional<EspacioResponse> espacio = this.espacioService.findEspacioById(id);
        if (espacio.isEmpty()) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND))
                    .body("No se ha encontrado el espacio buscado");
        }
        return ResponseEntity.ok(espacio.get());
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        List<EspacioResponse> espacios = this.espacioService.findAllEspacio();
        if(espacios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(espacios);
    }

    @GetMapping("{infix}")
    public ResponseEntity<?> findByNombreContainingIgnoreCase(
            @PathVariable String infix){
        List<EspacioResponse> espacios =
                this.espacioService.findByNombreContainingIgnoreCase(infix);
        if(espacios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(espacios);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EspacioRequest request){
        EspacioResponse espacio = espacioService.createEspacio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(espacio);
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody EspacioRequest request){
        try{
            EspacioResponse espacio =
                    espacioService.updateEspacio(id,request);
            return ResponseEntity.ok(espacio);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InvalidOperationException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(
                    "Ha ocurrido un error inesperado");
        }
    }
    @PatchMapping("{id}/activo")
    public ResponseEntity<?> cambiarActivo(@PathVariable("id") Long id,
                                          @RequestParam Boolean activo){
       EspacioResponse espacio = espacioService.cambiarActivo(id,activo);
       return ResponseEntity.ok(espacio);
    }


}
