package com.sistema.bitacora.repository;

import com.sistema.bitacora.dto.inventarioEspacio.InventarioEspacioResponse;
import com.sistema.bitacora.entity.InventarioEspacio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioEspacioRepository extends JpaRepository <InventarioEspacio,Long>{
    List<InventarioEspacioResponse> finByEspacioIdOrderByNombreItemAsc(Long espacioId);

    Optional<InventarioEspacioResponse> findByIdAndEspacio_Id(Long id, Long espacioId);

    List<InventarioEspacioResponse> findByEspacioIdAndEspacioBienId(
            Long espacioId,List<String>estados);
}
