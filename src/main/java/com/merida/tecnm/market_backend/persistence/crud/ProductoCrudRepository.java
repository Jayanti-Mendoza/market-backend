package com.merida.tecnm.market_backend.persistence.crud;

import com.merida.tecnm.market_backend.persistence.entity.Producto;
import org.springframework.data.repository.CrudRepository;

public interface ProductoCrudRepository extends CrudRepository<Producto, Integer> {
}