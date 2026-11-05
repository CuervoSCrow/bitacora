package com.sistema.bitacora.service;

import com.sistema.bitacora.dto.espacio.EspacioRequest;
import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.entity.Espacio;
import com.sistema.bitacora.exception.InvalidOperationException;
import com.sistema.bitacora.exception.ResourceNotFoundException;
import com.sistema.bitacora.repository.EspacioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public List<EspacioResponse> findByNombreContainingIgnoreCase(String infix) {
        Sort.Order order = Sort.Order.by("nombreEspacio")
                .with(Sort.Direction.ASC)
                .ignoreCase();
        Sort sort = Sort.by(order);
        return this.espacioRepository.findByNombreContainigIgnoreCase(infix,sort);
    }

    @Override
    public EspacioResponse createEspacio(EspacioRequest request) {
        Optional<EspacioResponse> espacioDB = findEspacioByNombre(request.getNombreEspacio());
        if(espacioDB.isPresent()){
            return espacioDB.get();
        }
        Espacio espacio = new Espacio(request);
        Espacio espacioNuevo = this.espacioRepository.save(espacio);
        return new EspacioResponse(espacioNuevo);

    }

    @Override
    public EspacioResponse updateEspacio(
            Long id, EspacioRequest request) {
        Optional<EspacioResponse> espacioDB=
                this.espacioRepository
                        .findEspacioById(id);
        if(espacioDB.isEmpty()){
            throw  new ResourceNotFoundException("No se puede efectuar la modificacion," +
                    "el espacio no existe.");
        }
        Optional<EspacioResponse> otroEspacio =
                this.espacioRepository.findEspacioByNombre(
                                            request.getNombreEspacio());
        if(otroEspacio.isPresent()&&
            !espacioDB.get().getId().equals(otroEspacio.get().getId())){
            throw new InvalidOperationException("No se puede efectuar la modificacion, " +
                    "el nombre del espacio ya existe");
        }
        Espacio espacio = new Espacio(espacioDB.get(),request);
        Espacio espacioModificar = this.espacioRepository.save(espacio);
        return new EspacioResponse(espacioModificar);
    }

    @Override
    public EspacioResponse cambiarActivo(Long id, Boolean activo) {

        if(activo==null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes indicar si el espacio estara activo");
        }
        Espacio espacio = this.espacioRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException(
                        "No se puede efectuar la modificación, el espacio no existe."
                ));
        espacio.setActivo(activo);
        Espacio espacioModificado = this.espacioRepository.save(espacio);
        return new EspacioResponse(espacioModificado);

    }
}
