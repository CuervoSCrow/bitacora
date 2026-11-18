package com.sistema.bitacora.entity.enums;

import lombok.Getter;

@Getter
public enum EstadoBien {
    BUENO("Bueno"),
    REGULAR("Regular"),
    MALO("Malo"),
    BAJA("Baja");

    private final String descripcion;

    EstadoBien(String descripcion){
        this.descripcion=descripcion;
    }

}
