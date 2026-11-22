package com.bazardigital.domain.factory;

import java.util.List;

import com.bazardigital.domain.model.Cliente;
import com.bazardigital.domain.model.ModalidadEntrega;
import com.bazardigital.domain.model.Pedido;
import com.bazardigital.domain.model.Producto;

public abstract class CreadorPedido {
    protected abstract ModalidadEntrega crearModalidad();

    public Pedido crearPedido(String id, Cliente cliente, List<Producto> productos) {
        return new Pedido(id, cliente, productos, crearModalidad());
    }
}
