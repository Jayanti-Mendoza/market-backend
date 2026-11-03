package com.merida.tecnm.market_backend.domain.service;

import com.merida.tecnm.market_backend.domain.Product;
import com.merida.tecnm.market_backend.domain.repository.ProductRepository;
import io.micrometer.observation.ObservationFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public List<Product> getAll(){
        return productRepository.getAll();
    }
    public Optional<Product> getProduct(int productId){
        return productRepository.getProduct(productId);
    }

    public Optional<List<Product>> getByCategory(int categoryId){
        return productRepository.getByCategory(categoryId);
    }

    public Product save (Product product){
        return productRepository.save(product);
    }

    public boolean delete(int productId){
        //verifica que existe antes de borrar
        if (getProduct(productId).isPresent()){
            productRepository.delete(productId);
            return true;
        }{
            return false;
        }
    }


}
