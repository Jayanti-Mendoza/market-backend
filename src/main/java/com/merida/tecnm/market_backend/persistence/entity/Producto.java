package com.merida.tecnm.market_backend.persistence.entity;

import jakarta.persistence.*;
@Entity
@Table(name ="productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    @Column(name ="id_producto")
    private Integer id_Producto;
    private String nombre;
    @Column (name ="id_categonia")
    private Integer id_categoria;
    @Column(name ="codigo_barras")
    private String codigoBarras;
    @Column (name ="precio_venta")
    private Double precio_Venta;
    @Column(name ="cantidad_stock")
    private Integer cantidadStock;
    private Boolean estado;
}