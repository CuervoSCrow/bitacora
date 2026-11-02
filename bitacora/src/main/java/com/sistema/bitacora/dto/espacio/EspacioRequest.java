package com.sistema.bitacora.dto.espacio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class EspacioRequest {
    private String nombreEspacio;
    private String ubicacion;
    private Integer capacidad;
    private String estadoActual;
    private String recursosBaseJson;
    private Boolean activo;
}
