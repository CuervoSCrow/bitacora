package com.sistema.bitacora.service;

import com.sistema.bitacora.dto.espacio.EspacioResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EspacioService {
    Optional<EspacioResponse> findEspacioById(Long id);
    Optional<EspacioResponse> findEspacioByNombre(String nombre);
    List<EspacioResponse> findAllEspacio();

}
