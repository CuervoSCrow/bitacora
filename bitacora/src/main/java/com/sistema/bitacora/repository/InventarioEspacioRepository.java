package com.sistema.bitacora.repository;

import com.sistema.bitacora.dto.inventarioEspacio.InventarioEspacioResponse;
import com.sistema.bitacora.entity.InventarioEspacio;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventarioEspacioRepository extends JpaRepository <InventarioEspacio,Long>{

    @Query("""
        SELECT new com.sistema.bitacora.dto.inventarioEspacio.InventarioEspacioResponse
        (i.id,i.nombreItem,i.descripcion,i.cantidad,i.estadoBien,i.fechaRegistro)
        FROM InventarioEspacio i
        WHERE i.id=:id
    """)
    List<InventarioEspacioResponse> finByEspacioId(Long espacioId, Sort sort);

    Optional<InventarioEspacioResponse> findByIdAndEspacio_Id(Long id, Long espacioId);

    List<InventarioEspacioResponse> findByEspacioIdAndEspacioBienId(
            Long espacioId,List<String>estados);
}
