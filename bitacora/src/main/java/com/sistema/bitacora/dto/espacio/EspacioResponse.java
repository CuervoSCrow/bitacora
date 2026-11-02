package com.sistema.bitacora.dto.espacio;

import com.sistema.bitacora.entity.Espacio;
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

    public EspacioResponse(Espacio espacio){
        this.id = espacio.getId();
        this.nombreEspacio = espacio.getNombreEspacio();
        this.ubicacion = espacio.getUbicacion();
        this.capacidad = espacio.getCapacidad();
        this.estadoActual = espacio.getEstadoActual();
        this.activo = espacio.getActivo();
    }
}
