package com.merida.tecnm.market_backend.web.controller;


import com.merida.tecnm.market_backend.domain.Product;
import com.merida.tecnm.market_backend.domain.service.ProductService;
import com.merida.tecnm.market_backend.persistence.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static aQute.bnd.annotation.headers.Resolution.optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private ProductoRepository productoRepository;


    @GetMapping("")
    public ResponseEntity <List<Product>> getAll(){
        return new ResponseEntity<>(productService.getAll() , HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int productId){
        return productService.getProduct(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable int categoryId){
        return productService.getByCategory(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int productId) {
        if (productService.delete(productId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
