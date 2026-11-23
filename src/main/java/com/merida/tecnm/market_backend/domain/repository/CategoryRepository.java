package com.merida.tecnm.market_backend.domain.repository;

import com.merida.tecnm.market_backend.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> getAll();
    Optional<Category> getCategory(int categoryId);
    Category save(Category category);
    void delete(int categoryId);
}
