package com.merida.tecnm.market_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merida.tecnm.market_backend.domain.Category;
import com.merida.tecnm.market_backend.domain.Client;
import com.merida.tecnm.market_backend.domain.Product;
import com.merida.tecnm.market_backend.domain.Purchase;
import com.merida.tecnm.market_backend.domain.PurchaseItem;
import com.merida.tecnm.market_backend.domain.repository.ProductRepository;
import com.merida.tecnm.market_backend.persistence.entity.Categoria;
import com.merida.tecnm.market_backend.persistence.entity.Cliente;
import com.merida.tecnm.market_backend.persistence.entity.Compra;
import com.merida.tecnm.market_backend.persistence.entity.CompraProducto;
import com.merida.tecnm.market_backend.persistence.entity.CompraProductoPK;
import com.merida.tecnm.market_backend.persistence.entity.mapper.CategoryMapper;
import com.merida.tecnm.market_backend.persistence.entity.mapper.ClientMapper;
import com.merida.tecnm.market_backend.persistence.entity.mapper.ProductMapper;
import com.merida.tecnm.market_backend.persistence.entity.mapper.PurchaseMapper;
import com.merida.tecnm.market_backend.web.controller.CategoryController;
import com.merida.tecnm.market_backend.web.controller.ClientController;
import com.merida.tecnm.market_backend.web.controller.ProductController;
import com.merida.tecnm.market_backend.web.controller.PurchaseController;
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

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ProductController productController;

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private ClientController clientController;

    @Autowired
    private PurchaseController purchaseController;

    @Autowired
    private com.merida.tecnm.market_backend.persistence.crud.ClienteCrudRepository clienteCrudRepository;

    @Autowired
    private com.merida.tecnm.market_backend.persistence.crud.ProductoCrudRepository productoCrudRepository;

    @Autowired
    private com.merida.tecnm.market_backend.domain.service.PurchaseService purchaseService;

    @Autowired
    private com.merida.tecnm.market_backend.persistence.crud.CompraCrudRepository compraCrudRepository;

    @Test
    @DisplayName("Debug test save purchase from prompt")
    void testSavePurchaseFromPrompt() throws Exception {
        System.out.println("=== PURCHASES IN DB ===");
        compraCrudRepository.findAll().forEach(compra -> {
            System.out.println("Compra ID: " + compra.getIdCompra() +
                    ", Client: " + compra.getIdCliente() +
                    ", MedioPago: '" + compra.getMedioPago() + "'" +
                    ", Estado: '" + compra.getEstado() + "'");
        });
        System.out.println("=== CLIENTS IN DB ===");
        clienteCrudRepository.findAll().forEach(c -> System.out.println("Client: " + c.getId() + " - " + c.getNombre()));

        System.out.println("=== PRODUCTS IN DB ===");
        productoCrudRepository.findAll().forEach(p -> System.out.println("Product: " + p.getIdProducto() + " - " + p.getNombre()));

        String json = """
                {
                   "idCliente": "CLI-001",
                   "medioPago": "TARJETA",
                   "comentario": "Entregar por la tarde",
                   "productosAdquiridos": [
                     {
                       "idProducto": 15,
                       "cantidad": 2,
                       "total": 500.0
                     },
                     {
                       "idProducto": 8,
                       "cantidad": 1,
                       "total": 150.0
                     }
                   ]
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper();
        Purchase purchase = objectMapper.readValue(json, Purchase.class);
        Purchase saved = purchaseService.save(purchase);
        assertNotNull(saved);
        assertNotNull(saved.getPurchaseId());
        assertEquals("CLI-001", saved.getClientId());
        assertEquals("T", saved.getPaymentMethod());
        assertEquals("Entregar por la tarde", saved.getComment());
        assertNotNull(saved.getItems());
        assertEquals(2, saved.getItems().size());
    }

    @Test
    @DisplayName("Context loads successfully and all controllers are injected")
    void contextLoads() {
        assertNotNull(productRepository);
        assertNotNull(categoryMapper);
        assertNotNull(productMapper);
        assertNotNull(purchaseMapper);
        assertNotNull(clientMapper);
        assertNotNull(productController);
        assertNotNull(categoryController);
        assertNotNull(clientController);
        assertNotNull(purchaseController);
    }

    @Test
    @DisplayName("Test CategoryMapper mapping and collection mapping")
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

        List<Categoria> list = List.of(categoria);
        List<Category> categories = categoryMapper.toCategories(list);
        assertNotNull(categories);
        assertEquals(1, categories.size());
    }

    @Test
    @DisplayName("Test ClientMapper mapping")
    void testClientMapper() {
        Cliente cliente = new Cliente();
        cliente.setId("CLI-001");
        cliente.setNombre("María");
        cliente.setApellidos("López");
        cliente.setCelular("9991112233");
        cliente.setDireccion("Calle 50 #200");
        cliente.setCorreoElectronico("maria.lopez@example.com");

        Client client = clientMapper.toClient(cliente);
        assertNotNull(client);
        assertEquals("CLI-001", client.getClientId());
        assertEquals("María", client.getName());
        assertEquals("López", client.getLastName());
        assertEquals("9991112233", client.getCellphone());
        assertEquals("Calle 50 #200", client.getAddress());
        assertEquals("maria.lopez@example.com", client.getEmail());

        Cliente inverse = clientMapper.toCliente(client);
        assertNotNull(inverse);
        assertEquals("CLI-001", inverse.getId());
        assertEquals("María", inverse.getNombre());
        assertEquals("López", inverse.getApellidos());
        assertEquals("9991112233", inverse.getCelular());
        assertEquals("Calle 50 #200", inverse.getDireccion());
        assertEquals("maria.lopez@example.com", inverse.getCorreoElectronico());
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

    @Test
    @DisplayName("Test PurchaseMapper converts domain Purchase to entity Compra (Maestro-Detalle)")
    void testPurchaseMapperInverseMaestroDetalle() {
        Purchase purchase = new Purchase();
        purchase.setClientId("CLI-001");
        purchase.setPaymentMethod("TARJETA");
        purchase.setComment("Entregar por la tarde");

        PurchaseItem item1 = new PurchaseItem();
        item1.setProductId(15);
        item1.setQuantity(2);
        item1.setTotal(500.0);

        PurchaseItem item2 = new PurchaseItem();
        item2.setProductId(8);
        item2.setQuantity(1);
        item2.setTotal(150.0);

        purchase.setItems(List.of(item1, item2));

        Compra compra = purchaseMapper.toCompra(purchase);
        assertNotNull(compra);
        assertEquals("CLI-001", compra.getIdCliente());
        assertEquals("TARJETA", compra.getMedioPago());
        assertEquals("Entregar por la tarde", compra.getComentario());
        assertNotNull(compra.getProductos());
        assertEquals(2, compra.getProductos().size());

        CompraProducto cp1 = compra.getProductos().get(0);
        assertNotNull(cp1.getId());
        assertEquals(15, cp1.getId().getIdProducto());
        assertEquals(2, cp1.getCantidad());
        assertEquals(500.0, cp1.getTotal());

        CompraProducto cp2 = compra.getProductos().get(1);
        assertNotNull(cp2.getId());
        assertEquals(8, cp2.getId().getIdProducto());
        assertEquals(1, cp2.getCantidad());
        assertEquals(150.0, cp2.getTotal());
    }

    @Test
    @DisplayName("Test deserialization of ADA 11 prompt JSON into Purchase DTO")
    void testJsonDeserializationOfComplexDto() throws Exception {
        String json = """
                {
                   "idCliente": "4546221",
                   "medioPago": "T",
                   "comentario": "Entregar por la tarde",
                   "productosAdquiridos": [
                     {
                       "idProducto": 15,
                       "cantidad": 2,
                       "total": 500.0
                     },
                     {
                       "idProducto": 8,
                       "cantidad": 1,
                       "total": 150.0
                     }
                   ]
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper();
        Purchase purchase = objectMapper.readValue(json, Purchase.class);

        assertNotNull(purchase);
        assertEquals("CLI-001", purchase.getClientId());
        assertEquals("TARJETA", purchase.getPaymentMethod());
        assertEquals("Entregar por la tarde", purchase.getComment());
        assertNotNull(purchase.getItems());
        assertEquals(2, purchase.getItems().size());

        PurchaseItem item1 = purchase.getItems().get(0);
        assertEquals(15, item1.getProductId());
        assertEquals(2, item1.getQuantity());
        assertEquals(500.0, item1.getTotal());

        PurchaseItem item2 = purchase.getItems().get(1);
        assertEquals(8, item2.getProductId());
        assertEquals(1, item2.getQuantity());
        assertEquals(150.0, item2.getTotal());
    }
}
