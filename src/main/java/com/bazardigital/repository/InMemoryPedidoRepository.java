package com.bazardigital.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bazardigital.domain.model.Pedido;

public class InMemoryPedidoRepository implements PedidoRepository {
    private final Map<String, Pedido> pedidos = new LinkedHashMap<>();

    @Override
    public void save(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido es obligatorio");
        }
        pedidos.put(pedido.getId(), pedido);
    }

    @Override
    public Optional<Pedido> findById(String id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    @Override
    public List<Pedido> findAll() {
        return new ArrayList<>(pedidos.values());
    }
}
