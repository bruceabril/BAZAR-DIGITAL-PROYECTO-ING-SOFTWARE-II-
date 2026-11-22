package com.bazardigital.domain.factory;

import com.bazardigital.domain.model.EntregaDomicilio;
import com.bazardigital.domain.model.ModalidadEntrega;

public class CreadorPedidoDomicilio extends CreadorPedido {
    @Override
    protected ModalidadEntrega crearModalidad() {
        return new EntregaDomicilio();
    }
}
