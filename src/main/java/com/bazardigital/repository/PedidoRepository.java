package com.bazardigital.repository;

import java.util.List;
import java.util.Optional;

import com.bazardigital.domain.model.Pedido;

public interface PedidoRepository {
    void save(Pedido pedido);

    Optional<Pedido> findById(String id);

    List<Pedido> findAll();
}
