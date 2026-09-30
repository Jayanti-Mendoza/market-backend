package com.merida.tecnm.market_backend.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table (name ="compras")
public class Compra{
@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
@Column (name="id_compra")
private Integer id_compra;
@Column (name ="id_cliente")
private String id_cliente;
private LocalDateTime fecha;
@Column (name ="medio_pago")
private String medioPago;
private String comentario;
private String estado;
}