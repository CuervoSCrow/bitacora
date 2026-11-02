package com.sistema.bitacora.repository;

import com.sistema.bitacora.dto.espacio.EspacioResponse;
import com.sistema.bitacora.entity.Espacio;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EspacioRepository extends
        JpaRepository<Espacio,Long> {

    @Query("""
            SELECT new com.sistema.bitacora.dto.espacio.EspacioResponse
            (e.id,e.nombreEspacio,e.ubicacion,e.capacidad,e.estadoActual,e.activo)
            FROM Espacio e
            WHERE e.id = :id
            """)
    Optional<EspacioResponse> findEspacioById(@Param("id") Long id);

    @Query("""
            SELECT new com.sistema.bitacora.dto.espacio.EspacioResponse
            (e.id,e.nombreEspacio,e.ubicacion,e.capacidad,e.estadoActual,e.activo)
            FROM Espacio e
            WHERE e.nombreEspacio = :nombre
            """)
    Optional<EspacioResponse>findEspacioByNombre(@Param("nombre") String nombre);

    @Query("""
            SELECT new com.sistema.bitacora.dto.espacio.EspacioResponse
            (e.id, e.nombreEspacio,e.ubicacion,e.capacidad,e.estadoActual,e.activo)
            FROM Espacio e
            """)
    List<EspacioResponse> findAllEspacio(Sort sort);

    @Query("""
            SELECT new com.sistema.bitacora.dto.espacio.EspacioResponse
            (e.id, e.nombreEspacio,e.ubicacion,e.capacidad,e.estadoActual,e.activo)
            FROM Espacio e
            WHERE UPPER(e.nombreEspacio) LIKE UPPER(CONCAT('%',:infix,'%'))            
            """)
    List<EspacioResponse> findByNombreContainigIgnoreCase(
            @Param("infix") String infix, Sort sort);

    List<Espacio> findByActivoTrueOrderByNombreEspacioAsc();



    boolean existsByNombreEspacioIgnoreCaseAndIdNot(
            String nombreEspacio,
            Long id
    );
}
