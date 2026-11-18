package com.sistema.bitacora.dto.inventarioEspacio;

import com.sistema.bitacora.entity.enums.EstadoBien;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InventarioEspacioRequest {
    @NotBlank(message = "El nombre del item es obligatorio")
    @Size(max=150,message = "El nombre no puede exceder 150 caracteres")
    private String nombreItem;

    @Size(max=2000,message="La descripcion es demasiado larga.")
    @Min(value=1,message="La cantidad debe ser al menos 1")
    private Integer cantidad;

    private EstadoBien estadoBien;

    @NotNull(message = "El espacio es obligatorio")
    private Long espacioId;
}
