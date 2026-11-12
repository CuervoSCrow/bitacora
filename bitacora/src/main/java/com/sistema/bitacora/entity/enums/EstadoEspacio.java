package com.sistema.bitacora.entity.enums;

public enum EstadoEspacio {
    DISPONIBLE("Disponible"),
    OCUPADO("Ocupado"),
    MANTENIMIENTO("En mantenimiento"),
    INACTIVO("Inactivo");

    private final String descripcion;

    EstadoEspacio(String descripcion){
        this.descripcion = descripcion;
    }
    public String getDescripcion(){
        return descripcion;
    }
}
