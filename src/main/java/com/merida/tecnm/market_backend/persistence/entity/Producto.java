package com.merida.tecnm.market_backend.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name ="productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    @Column(name ="id_producto")
    private Integer idProducto;
    private String nombre;
    @Column (name ="id_categoria")
    private Integer id_categoria;
    @Column(name ="codigo_barras")
    private String codigoBarras;
    @Column (name ="precio_venta")
    private Double precio_Venta;
    @Column(name ="cantidad_stock")
    private Integer cantidadStock;
    private Boolean estado;


    @ManyToOne
    @JoinColumn (name ="id_categorias", insertable = false, updatable = false)
    private Categoria categoria;

    //ojo
    @OneToMany (mappedBy = "productos")
    private List<CompraProducto> compraProductos;
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(Integer id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Double getPrecio_Venta() {
        return precio_Venta;
    }

    public void setPrecio_Venta(Double precio_Venta) {
        this.precio_Venta = precio_Venta;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}