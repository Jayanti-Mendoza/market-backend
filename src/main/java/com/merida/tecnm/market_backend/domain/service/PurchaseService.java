package com.merida.tecnm.market_backend.domain.service;

import com.merida.tecnm.market_backend.domain.Purchase;
import com.merida.tecnm.market_backend.domain.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class PurchaseService {

    private PurchaseRepository purchaseRepository;
    public List<Purchase> getALL(){
        return purchaseRepository.getAll();

    }
public Optional<List<Purchase>> getByClient(String clientId){
        return purchaseRepository.getByClient(clientId);
}
public Purchase save (Purchase purchase){
        return purchaseRepository.save(purchase);
}
}
