package com.merida.tecnm.market_backend.persistence.entity;

import com.merida.tecnm.market_backend.persistence.crud.ProductoCrudRepository;
import com.merida.tecnm.market_backend.persistence.entity.Producto;

import java.util.List;
public class ProductoRepository {

    private ProductoCrudRepository productoCrudRepository;

    //SELECT * FROM Productos
    public List<Producto> getAll(){
        //Vamos a "castear"
        return (List<Producto>) productoCrudRepository.findAll();
    }
}