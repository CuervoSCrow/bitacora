package com.sistema.bitacora.entity;

import com.sistema.bitacora.dto.espacio.EspacioRequest;
import com.sistema.bitacora.dto.espacio.EspacioResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "espacios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_espacio",
            nullable = false,
            unique = true,
            length = 100)
    private String nombreEspacio;

    @Column(nullable = false, length = 150)
    private String ubicacion;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(name = "estado_actual", length = 30)
    private String estadoActual = "DISPONIBLE";

    @Column
    private Boolean activo = true;

    @Column(name = "recursos_base_json", columnDefinition = "TEXT")
    private String recursosBaseJson;



    public Espacio(EspacioRequest request){
        this.nombreEspacio = request.getNombreEspacio();
        this.ubicacion = request.getUbicacion();
        this.capacidad = request.getCapacidad();
        this.estadoActual= request.getEstadoActual();
        this.activo=true;
    }

    public Espacio(EspacioResponse response,
                   EspacioRequest request){
        this.id = response.getId();
        this.nombreEspacio= request.getNombreEspacio();
        this.ubicacion= request.getUbicacion();
        this.capacidad= request.getCapacidad();
        this.estadoActual= request.getEstadoActual();
        this.activo=request.getActivo();
    }
}
