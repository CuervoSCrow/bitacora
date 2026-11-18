package com.sistema.bitacora.entity;

import com.sistema.bitacora.entity.enums.EstadoBien;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="inventario_espacios")
@Getter @Setter
@NoArgsConstructor
public class InventarioEspacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre_item",nullable = false,length = 150)
    private String nombreItem;

    @Column(columnDefinition="TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad =1;

    @Enumerated(EnumType.STRING)
    @Column(name="estado_bien", length = 50)
    private EstadoBien estadoBien= EstadoBien.BUENO;

    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="espacio_id",nullable = false)
    private Espacio espacio;


}
