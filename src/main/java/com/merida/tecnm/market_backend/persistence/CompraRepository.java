package com.merida.tecnm.market_backend.persistence;

import com.merida.tecnm.market_backend.domain.Purchase;
import com.merida.tecnm.market_backend.domain.repository.PurchaseRepository;
import com.merida.tecnm.market_backend.persistence.crud.CompraCrudRepository;
import com.merida.tecnm.market_backend.persistence.entity.Compra;
import com.merida.tecnm.market_backend.persistence.entity.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompraRepository implements PurchaseRepository {

    @Autowired
    private CompraCrudRepository compraCrudRepository;

    @Autowired
    private com.merida.tecnm.market_backend.persistence.crud.ClienteCrudRepository clienteCrudRepository;

    @Autowired
    private PurchaseMapper mapper;

    @Override
    public List<Purchase> getAll() {
        return mapper.toPurchases((List<Compra>) compraCrudRepository.findAll());
    }

    @Override
    public Optional<Purchase> getPurchase(int purchaseId) {
        return compraCrudRepository.findById(purchaseId).map(mapper::toPurchase);
    }

    @Override
    public Optional<List<Purchase>> getByClient(String clientId) {
        return compraCrudRepository.findByIdCliente(clientId)
                .map(mapper::toPurchases);
    }

    @Override
    public Purchase save(Purchase purchase) {
        Compra compra = mapper.toCompra(purchase);
        if (compra.getFecha() == null) {
            compra.setFecha(java.time.LocalDateTime.now());
        }
        if (compra.getEstado() == null) {
            compra.setEstado("P");
        } else if (compra.getEstado().length() > 1) {
            compra.setEstado(compra.getEstado().substring(0, 1).toUpperCase());
        }
        if (compra.getMedioPago() != null && compra.getMedioPago().length() > 1) {
            compra.setMedioPago(compra.getMedioPago().substring(0, 1).toUpperCase());
        }
        if (compra.getIdCliente() != null && !clienteCrudRepository.existsById(compra.getIdCliente())) {
            com.merida.tecnm.market_backend.persistence.entity.Cliente nuevoCliente =
                    new com.merida.tecnm.market_backend.persistence.entity.Cliente();
            nuevoCliente.setId(compra.getIdCliente());
            nuevoCliente.setNombre("Cliente " + compra.getIdCliente());
            nuevoCliente.setApellidos("Registrado");
            clienteCrudRepository.save(nuevoCliente);
        }
        if (compra.getProductos() != null) {
            compra.getProductos().forEach(product -> {
                if (product.getId() == null) {
                    product.setId(new com.merida.tecnm.market_backend.persistence.entity.CompraProductoPK());
                }
                if (product.getEstado() == null) {
                    product.setEstado(true);
                }
                product.setCompra(compra);
            });
        }
        return mapper.toPurchase(compraCrudRepository.save(compra));
    }
}
