package com.merida.tecnm.market_backend.persistence.crud;

import com.merida.tecnm.market_backend.persistence.entity.Producto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//Métodos abstractos que despues se implementará
public interface ProductoCrudRepository extends CrudRepository<Producto, Integer> {
 /*SQL Query
 SELECT
 FROM productos
WHERE id_categoria = 10?
  ORDER BY nombre ASC
  */
  List<Producto> findByIdCategoriaOrderByNombreAsc(int idCategoria);

  //Cantidad Stock
  Optional<List<Producto>> findByCantidadStockLessThenAnsEstado(int cantidadStock, boolean estado);

    Optional<List<Producto>> findByCantidadStockLessThanAndEstado(int cantidad, boolean b);
}