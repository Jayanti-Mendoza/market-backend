package com.merida.tecnm.market_backend.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Product {
    @JsonAlias({"idProducto", "id_producto"})
    private Integer productId;

    @JsonAlias({"nombre"})
    private String name;

    @JsonAlias({"idCategoria", "id_categoria"})
    private Integer categoryId;

    @JsonAlias({"precio", "precioVenta", "precio_venta"})
    private double price;

    @JsonAlias({"cantidadStock", "cantidad_stock"})
    private int stock;

    @JsonAlias({"estado"})
    private boolean active;

    @JsonAlias({"categoria"})
    private Category category;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}