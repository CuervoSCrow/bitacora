package com.sistema.bitacora.service;

import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.repository.EspacioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspacioServiceImpl implements EspacioService {

    private final EspacioRepository espacioRepository;

    @Override
    public Optional<EspacioResponse> findEspacioById(Long id) {
        return this.espacioRepository.findEspacioById(id);
    }

    @Override
    public Optional<EspacioResponse> findEspacioByNombre(String nombre) {
        return this.espacioRepository.findEspacioByNombre(nombre);
    }

    @Override
    public List<EspacioResponse> findAllEspacio() {
        Sort sort = Sort.by("nombreEspacio").ascending();
        return this.espacioRepository.findAllEspacio(sort);
    }
}
