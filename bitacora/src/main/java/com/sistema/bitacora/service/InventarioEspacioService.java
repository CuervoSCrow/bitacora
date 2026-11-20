package com.sistema.bitacora.service;

import com.sistema.bitacora.dto.inventarioEspacio.InventarioEspacioResponse;
import com.sistema.bitacora.entity.InventarioEspacio;

import java.util.List;

public interface InventarioEspacioService {
    public List<InventarioEspacioResponse> listar(Long espacioId);

}
