package com.sistema.bitacora.entity.enums;

import jakarta.persistence.AttributeConverter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EstadoEspacioConverter implements
        AttributeConverter<EstadoEspacio,String> {
    private static final Logger log = LoggerFactory.getLogger(EstadoEspacioConverter.class);

    @Override
    public String convertToDatabaseColumn(EstadoEspacio attribute) {
        if(attribute==null)return null;
        return attribute.name().toLowerCase();
    }

    @Override
    public EstadoEspacio convertToEntityAttribute(String dbData) {
        if(dbData==null)return null;
        try{
            return EstadoEspacio.valueOf(dbData.trim().toUpperCase());
        }catch(IllegalArgumentException e){
            log.warn("Valor de enum desconocido en la bse de datos: {}",dbData);
            return EstadoEspacio.DISPONIBLE; // o null segun tu logica
        }
    }
}
