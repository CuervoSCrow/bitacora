package com.sistema.bitacora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departamentos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column( name="nombre_departamento",nullable = false,
    unique = true,length = 100)
    private String nombreDepartamento;

    @Column(name="clave_departamento",length = 20)
    private String claveDepartamento;

    @Column(nullable = false)
    private Boolean activo=true;

    @Column(name="cupo_mensual",nullable = false)
    private Integer cupoMensual=1;
}
