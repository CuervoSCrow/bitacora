package com.sistema.bitacora.dto.espacio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EspacioResponse {
    private Long id;
    private String nombreEspacio;
    private String ubicacion;
    private Integer capacidad;
    private String estadoActual;
    //    private String recursosBaseJson;
    private boolean activo;
}
