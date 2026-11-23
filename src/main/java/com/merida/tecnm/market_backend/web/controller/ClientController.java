package com.merida.tecnm.market_backend.web.controller;

import com.merida.tecnm.market_backend.domain.Client;
import com.merida.tecnm.market_backend.domain.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/clients", "/clientes"})
@Tag(name = "Client", description = "Manage supermarket clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    @Operation(summary = "Get all clients",
            description = "Return a list of all registered clients")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of clients")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<Client>> getAll() {
        return new ResponseEntity<>(clientService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID",
            description = "Return a client by their unique identifier")
    @ApiResponse(responseCode = "200", description = "Client found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Client> getClient(
            @Parameter(description = "ID of the client", example = "CLI-001", required = true)
            @PathVariable("id") String clientId) {
        return clientService.getClient(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a new client",
            description = "Register a new client and return it",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Example Client",
                                    value = """
                                            {
                                                "clientId": "CLI-001",
                                                "name": "Juan",
                                                "lastName": "Pérez",
                                                "cellphone": "9991234567",
                                                "address": "Calle 60 #123, Mérida",
                                                "email": "juan.perez@example.com"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Client created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid client data")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Client> save(@RequestBody Client client) {
        return new ResponseEntity<>(clientService.save(client), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing client",
            description = "Updates the client corresponding to the provided ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Example Client Update",
                                    value = """
                                            {
                                                "name": "Juan Carlos",
                                                "lastName": "Pérez Gómez",
                                                "cellphone": "9999876543",
                                                "address": "Calle 60 #123, Mérida, Yucatán",
                                                "email": "juancarlos.perez@example.com"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Client updated successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Client> update(
            @Parameter(description = "ID of the client to update", example = "CLI-001", required = true)
            @PathVariable("id") String clientId,
            @RequestBody Client client) {
        return clientService.update(clientId, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client by ID",
            description = "Deletes a client if it exists")
    @ApiResponse(responseCode = "200", description = "Client deleted successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @ApiResponse(responseCode = "409", description = "Cannot delete client because it has associated purchases")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the client to be deleted",
                    example = "CLI-001", required = true)
            @PathVariable("id") String clientId) {
        try {
            if (clientService.delete(clientId)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
