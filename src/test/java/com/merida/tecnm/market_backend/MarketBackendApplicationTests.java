package com.merida.tecnm.market_backend;

import com.merida.tecnm.market_backend.domain.Category;
import com.merida.tecnm.market_backend.domain.Product;
import com.merida.tecnm.market_backend.domain.Purchase;
import com.merida.tecnm.market_backend.domain.PurchaseItem;
import com.merida.tecnm.market_backend.domain.repository.ProductRepository;
import com.merida.tecnm.market_backend.persistence.entity.Categoria;
import com.merida.tecnm.market_backend.persistence.entity.Compra;
import com.merida.tecnm.market_backend.persistence.entity.CompraProducto;
import com.merida.tecnm.market_backend.persistence.entity.CompraProductoPK;
import com.merida.tecnm.market_backend.persistence.entity.mapper.CategoryMapper;
import com.merida.tecnm.market_backend.persistence.entity.mapper.ProductMapper;
import com.merida.tecnm.market_backend.persistence.entity.mapper.PurchaseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketBackendApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Test
    @DisplayName("Context loads successfully")
    void contextLoads() {
        assertNotNull(productRepository);
        assertNotNull(categoryMapper);
        assertNotNull(productMapper);
        assertNotNull(purchaseMapper);
    }

    @Test
    @DisplayName("Test CategoryMapper mapping")
    void testCategoryMapper() {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setDescripcion("Frutas y verduras");
        categoria.setEstado(true);

        Category category = categoryMapper.toCategory(categoria);

        assertNotNull(category);
        assertEquals(1, category.getCategoryId());
        assertEquals("Frutas y verduras", category.getCategory());
        assertTrue(category.isActive());

        Categoria inverse = categoryMapper.toCategoria(category);
        assertNotNull(inverse);
        assertEquals(1, inverse.getIdCategoria());
        assertEquals("Frutas y verduras", inverse.getDescripcion());
        assertTrue(inverse.getEstado());
    }

    @Test
    @DisplayName("Test querying products by Category")
    void testGetProductsByCategory() {
        Optional<List<Product>> productsOpt = productRepository.getByCategory(1);

        assertTrue(productsOpt.isPresent(), "Category 1 should return products");
        List<Product> products = productsOpt.get();
        assertFalse(products.isEmpty(), "Products list for category 1 should not be empty");

        for (Product product : products) {
            assertEquals(1, product.getCategoryId(), "Product categoryId must match requested category");
            assertNotNull(product.getName());
            assertNotNull(product.getCategory(), "Embedded Category should be mapped");
            assertEquals(1, product.getCategory().getCategoryId());
        }
    }

    @Test
    @DisplayName("Test getting all products")
    void testGetAllProducts() {
        List<Product> products = productRepository.getAll();
        assertNotNull(products);
        assertFalse(products.isEmpty(), "Should retrieve existing products from the database");
    }

    @Test
    @DisplayName("Test PurchaseMapper converts items accurately")
    void testPurchaseMapper() {
        Compra compra = new Compra();
        compra.setIdCompra(10);
        compra.setIdCliente("C123");
        compra.setFecha(LocalDateTime.now());
        compra.setMedioPago("E");
        compra.setComentario("Prueba");
        compra.setEstado("A");

        CompraProducto item = new CompraProducto();
        CompraProductoPK pk = new CompraProductoPK();
        pk.setIdCompra(10);
        pk.setIdProducto(1);
        item.setId(pk);
        item.setCantidad(5);
        item.setTotal(1500.0);
        item.setEstado(true);

        List<CompraProducto> productos = new ArrayList<>();
        productos.add(item);
        compra.setProductos(productos);

        Purchase purchase = purchaseMapper.toPurchase(compra);

        assertNotNull(purchase);
        assertEquals(10, purchase.getPurchaseId());
        assertEquals("C123", purchase.getClientId());
        assertNotNull(purchase.getItems());
        assertEquals(1, purchase.getItems().size());

        PurchaseItem pItem = purchase.getItems().get(0);
        assertEquals(1, pItem.getProductId());
        assertEquals(5, pItem.getQuantity());
        assertEquals(1500.0, pItem.getTotal());
        assertTrue(pItem.isActive());
    }
}
