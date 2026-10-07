package com.merida.tecnm.market_backend.persistence.entity;

import com.merida.tecnm.market_backend.persistence.crud.ProductoCrudRepository;
import com.merida.tecnm.market_backend.persistence.entity.Producto;

import java.util.List;
import java.util.Optional;

public class ProductoRepository {

    private ProductoCrudRepository productoCrudRepository;
    private int IdCategoria;

    //SELECT * FROM Productos
    public List<Producto> getAll(){
        //Vamos a "castear"
        return (List<Producto>) productoCrudRepository.findAll();
    }

    public List<Producto> getByCategory(int idCategoria){
        return productoCrudRepository.findByIdCategoriaOrderByNombreAsc(IdCategoria);
    }
    public Optional<List<Producto>> getEscasos(int cantidad){
        return productoCrudRepository.findByCantidadStockLessThanAndEstado(cantidad,true);
    }
    Producto (int idProducto) {
        return productoCrudRepository.findById(idProducto);
    }
    public Producto save(Producto producto){
        return productoCrudRepository.save(producto);
    }
    public void delete (int idProduct){
        productoCrudRepository.deleteById(idProduct);
    }
}