package com.sistema.bitacora.service;

import com.sistema.bitacora.dto.espacio.EspacioRequest;
import com.sistema.bitacora.dto.espacio.EspacioResponse;

import java.util.List;
import java.util.Optional;

public interface EspacioService {
    Optional<EspacioResponse> findEspacioById(Long id);
    Optional<EspacioResponse> findEspacioByNombre(String nombre);
    List<EspacioResponse> findAllEspacio();
    List<EspacioResponse> findByNombreContainingIgnoreCase(String infix);
    EspacioResponse createEspacio(EspacioRequest request);
    EspacioResponse updateEspacio(Long id,EspacioRequest request);
    EspacioResponse cambiarActivo(Long id,Boolean activo);

}
