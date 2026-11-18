package com.sistema.bitacora.service;

import com.sistema.bitacora.entity.InventarioEspacio;

import java.util.List;

public interface InventarioEspacioService {
    public List<InventarioEspacio> listar(Long espacioId);

}
