package com.sistema.bitacora.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="inventario_espacios")
@Getter @Setter
public class InventarioEspacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre_item",nullable = false,length = 150)
    private String nombreItem;

    @Column(columnDefinition=Te)

}
