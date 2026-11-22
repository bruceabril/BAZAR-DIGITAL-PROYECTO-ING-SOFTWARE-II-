package com.bazardigital.repository;

import java.util.List;
import java.util.Optional;

import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.model.Producto;

public interface ProductoRepository {
    List<Producto> findAll();

    List<Producto> findByCategoria(Categoria categoria);

    Optional<Producto> findById(String id);

    void save(Producto producto);
}
