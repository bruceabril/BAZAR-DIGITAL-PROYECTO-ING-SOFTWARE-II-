package com.bazardigital.domain.observer;

import com.bazardigital.domain.model.EstadoPedido;
import com.bazardigital.domain.model.Pedido;

@FunctionalInterface
public interface ObservadorPedido {
    void alCambiarEstado(Pedido pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo);
}
