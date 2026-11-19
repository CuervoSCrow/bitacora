package com.sistema.bitacora.service;

import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.dto.inventarioEspacio.InventarioEspacioResponse;
import com.sistema.bitacora.entity.Espacio;
import com.sistema.bitacora.repository.EspacioRepository;
import com.sistema.bitacora.repository.InventarioEspacioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

 @Service
 @RequiredArgsConstructor
public class InventarioEspacioServiceImpl implements InventarioEspacioService{

    private final InventarioEspacioRepository inventarioEspacioRepository;
    private final EspacioRepository espacioRepository;
    private final EspacioService espacioService;


    @Override
    public List<InventarioEspacioResponse> listar(Long espacioId) {
        Sort.Order order = Sort.Order.by("nombreItem")
                .with(Sort.Direction.ASC)
                .ignoreCase();
        Sort sort = Sort.by(order);
        return this.inventarioEspacioRepository
                .finByEspacioIdOrderByNombreItemAsc(espacioId,sort);
    }

    private EspacioResponse buscarEspacio(Long espacioId){
        if(espacioId==null || espacioId<=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Identificador de espacio invalido"
            );
        }
        return espacioService.findEspacioById(espacioId)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El espacio no existe"
        ));
    }
}
