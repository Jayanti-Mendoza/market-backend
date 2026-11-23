package com.merida.tecnm.market_backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record LoginRequest(
        @JsonAlias({"username", "user", "email", "correo", "id", "idCliente"})
        String correoElectronico,
        String password
) {}
