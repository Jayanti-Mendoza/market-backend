package com.merida.tecnm.market_backend.persistence.entity.mapper;
import com.merida.tecnm.market_backend.domain.Category;
import com.merida.tecnm.market_backend.persistence.entity.Categoria;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mappings({
            @Mapping(source = "idCategoria", target = "categoryId"),
            @Mapping(source = "description", target = "catefoy"),
            @Mapping(source = "estado", target = "active")
    })
    Category toCategory (Categoria categoria);
    @InheritInverseConfiguration
    @Mapping(target = "productos", ignore =true)
    Category toCategoria (Category category);
}
