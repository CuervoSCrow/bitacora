package com.sistema.bitacora.dto.inventarioEspacio;

import com.sistema.bitacora.entity.InventarioEspacio;
import com.sistema.bitacora.entity.enums.EstadoBien;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioEspacioResponse {
    private Long id;
    private String nombreItem;
    private String descripcion;
    private Integer cantidad;
    private EstadoBien estadoBien;
    private LocalDateTime fechaRegistro;

    public InventarioEspacioResponse(
            InventarioEspacio inventarioEspacio) {
        this.id = inventarioEspacio.getId();
        this.nombreItem = inventarioEspacio.getNombreItem();
        this.descripcion = inventarioEspacio.getDescripcion();
        this.cantidad = inventarioEspacio.getCantidad();
        this.estadoBien = inventarioEspacio.getEstadoBien();
        this.fechaRegistro = inventarioEspacio.getFechaRegistro();
    }
}
