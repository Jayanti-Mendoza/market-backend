package com.merida.tecnm.market_backend.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public class PurchaseItem {

    @JsonAlias({"idProducto", "id_producto"})
    private int productId;

    @JsonAlias({"cantidad"})
    private int quantity;

    @JsonAlias({"total"})
    private double total;

    @JsonAlias({"estado"})
    private boolean active = true;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
