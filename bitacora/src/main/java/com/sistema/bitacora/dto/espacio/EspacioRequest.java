package com.sistema.bitacora.dto.espacio;

import com.sistema.bitacora.entity.enums.EstadoEspacio;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EspacioRequest {
    @NotBlank(message = "El nombre del espacio es obligatorio")
    @Size(min=2,max=100,message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombreEspacio;

    @NotBlank(message = "La ubicación es obligatorio.")
    @Size(max=150,message = "La ubicación no puede superar 150 caracteres")
    private String ubicacion;

    @NotNull(message = "La Capacidad es obligatorio.")
    @Min(value=1,message = "La Capacidad debe ser mayor a 0")
    @Max(value=1000, message="La capacidad no puede superar 1000")
    private Integer capacidad;

    @NotNull(message = "El espacio actual es obligatorio")
    private EstadoEspacio estadoActual;

    @NotNull(message = "El campo activo es obligatorio.")
    private Boolean activo;
}
