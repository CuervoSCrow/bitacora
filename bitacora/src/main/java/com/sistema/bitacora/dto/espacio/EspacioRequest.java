package com.sistema.bitacora.dto.espacio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class EspacioRequest {
    private Integer capacidad;
    private String estadoActual;
    private String recursosBaseJson;
    private boolean activo;
}
