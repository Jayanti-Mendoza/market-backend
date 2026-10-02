package com.merida.tecnm.market_backend.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

//Relación con el cliente;
    // Mucho compras para un cliente

    @ManyToMany
    @JoinColumn(name ="id_cliente", insertable = false, updatable= false)
    private Cliente cliente;

    //Relación con compraproducto
    @OneToMany (mappedBy ="compra")
    private List<CompraProducto> productos;


    public void setId_compra(Integer id_compra) {
        this.id_compra = id_compra;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}