package com.sistema.bitacora.dto.inventarioEspacio;

import com.sistema.bitacora.entity.enums.EstadoBien;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class InventarioEspacioResponse {
    private Long id;
    private String nombreItem;
    private String descripcion;
    private Integer cantidad;
    private EstadoBien estadoBien;
    private LocalDateTime fechaRegistro;

//    Datosdel espacio "aplanados" para evitar ciclos y lazy loading
    private Long espacioId;
    private String espacioNombre;
}
