package com.merida.tecnm.market_backend.persistence.entity.mapper;

import com.merida.tecnm.market_backend.domain.Purchase;
import com.merida.tecnm.market_backend.domain.PurchaseItem;
import com.merida.tecnm.market_backend.persistence.entity.CompraProducto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Mapper(componentModel = "spring", uses ={ProductMapper.class})
public interface PurchaseItemMapper {
    @Mappings({
            @Mapping(source = "id.idProducto", target = "productId"),
            @Mapping(source = "cantidad", target = "quantity"),
            @Mapping(source = "estado" , target = "active"),
    })
    PurchaseItem toPurchaseItem(CompraProducto compra);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(target = "id.idCompra", ignore = true),
            @Mapping(target = "compra", ignore = true),
            @Mapping(target = "producto" , ignore = true),
    })
    CompraProducto toCompraProducto(PurchaseItem item);
}
