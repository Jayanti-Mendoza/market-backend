package com.merida.tecnm.market_backend.domain.service;

import com.merida.tecnm.market_backend.domain.Product;
import com.merida.tecnm.market_backend.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.getAll();
    }

    public Optional<Product> getProduct(int productId) {
        return productRepository.getProduct(productId);
    }

    public Optional<List<Product>> getByCategory(int categoryId) {
        return productRepository.getByCategory(categoryId);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> update(int productId, Product product) {
        return getProduct(productId).map(existing -> {
            product.setProductId(productId);
            return productRepository.save(product);
        });
    }

    public boolean delete(int productId) {
        if (getProduct(productId).isPresent()) {
            productRepository.delete(productId);
            return true;
        } else {
            return false;
        }
    }
}
