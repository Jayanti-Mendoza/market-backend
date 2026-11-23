package com.merida.tecnm.market_backend.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Client {

    @JsonAlias({"id", "idCliente", "id_cliente"})
    private String clientId;

    @JsonAlias({"nombre", "firstName"})
    private String name;

    @JsonAlias({"apellidos", "apellido", "lastName"})
    private String lastName;

    @JsonAlias({"celular", "telefono", "phone"})
    private String cellphone;

    @JsonAlias({"direccion", "address"})
    private String address;

    @JsonAlias({"correoElectronico", "correo_electronico", "correo", "email"})
    private String email;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
