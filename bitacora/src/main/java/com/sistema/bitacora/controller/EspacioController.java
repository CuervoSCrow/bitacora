package com.sistema.bitacora.controller;


import com.sistema.bitacora.dto.espacio.EspacioRequest;
import com.sistema.bitacora.dto.espacio.EspacioResponse;

import com.sistema.bitacora.service.EspacioServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("{id}")
    public ResponseEntity<?> findBYId(@PathVariable Long id) {
        Optional<EspacioResponse> espacio = this.espacioService.findEspacioById(id);
        if (espacio.isEmpty()) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND))
                    .body("No se ha encontrado el espacio buscado");
        }
        return ResponseEntity.ok(espacio.get());
    }

    @GetMapping()
    public ResponseEntity<?> findAll(){
        List<EspacioResponse> espacios = this.espacioService.findAllEspacio();
        if(espacios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(espacios);
    }

    @GetMapping("/find/{infix}")
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
    public ResponseEntity<?> create(@RequestBody @Valid EspacioRequest request){
        EspacioResponse espacio = espacioService.createEspacio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(espacio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspacioResponse> update(@PathVariable Long id,
                                    @RequestBody @Valid EspacioRequest request){
       return ResponseEntity.ok(espacioService.updateEspacio(id,request));
    }
    @PatchMapping("{id}/activo")
    public ResponseEntity<?> cambiarActivo(@PathVariable("id") Long id,
                                          @RequestParam Boolean activo){
       EspacioResponse espacio = espacioService.cambiarActivo(id,activo);
       return ResponseEntity.ok(espacio);
    }
    //metodo que suma 2 numeros







}
