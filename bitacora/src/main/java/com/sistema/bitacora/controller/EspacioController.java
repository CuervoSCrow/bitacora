package com.sistema.bitacora.controller;


import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.service.EspacioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/espacios")
//@PreAuthorize("hasRole('ADMINISTRADOR')")
@RequiredArgsConstructor
public class EspacioController {

    private final EspacioServiceImpl espacioService;

    @GetMapping("/{nombre}")
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
}
