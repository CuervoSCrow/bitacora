package com.sistema.bitacora.support;

import com.sistema.bitacora.entity.Espacio;
import com.sistema.bitacora.entity.enums.EstadoEspacio;

/** Test data builder para la entidad {@link Espacio}.
        * Permite construir instancias con valores por defecto válidos y sobrescribir
 * solo los campos relevantes para cada caso de prueba.
 **/
public class EspacioTestDataBuilder {
    private String nombreEspacio="Sala Default";
    private String ubicacion="Piso 0";
    private Integer capacidad=10;
    private EstadoEspacio estadoActual= EstadoEspacio.DISPONIBLE;
    private Boolean activo = Boolean.TRUE;

    private EspacioTestDataBuilder(){}

    public static EspacioTestDataBuilder anEspacio(){
        return new EspacioTestDataBuilder();
    }

    public EspacioTestDataBuilder withNombreEspacio(String nombreEspacio){
        this.nombreEspacio=nombreEspacio;
        return this;
    }

    public EspacioTestDataBuilder withUbicacion(String ubicacion){
        this.ubicacion=ubicacion;
        return this;
    }

    public EspacioTestDataBuilder withCapacidad(Integer capacidad){
        this.capacidad=capacidad;
        return this;
    }

    public EspacioTestDataBuilder withEstadoActual(EstadoEspacio estadoActual){
                this.estadoActual=estadoActual;
        return this;
    }

    public EspacioTestDataBuilder withActivo(Boolean activo){
        this.activo=activo;
        return this;
    }

    public Espacio build(){
        Espacio espacio = new Espacio();
        espacio.setNombreEspacio(nombreEspacio);
        espacio.setUbicacion(ubicacion);
        espacio.setCapacidad(capacidad);
        espacio.setEstadoActual(estadoActual);
        espacio.setActivo(activo);
        return espacio;
    }

}
