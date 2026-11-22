package com.bazardigital.domain.factory;

import com.bazardigital.domain.model.EntregaRetiro;
import com.bazardigital.domain.model.ModalidadEntrega;

public class CreadorPedidoRetiro extends CreadorPedido {
    @Override
    protected ModalidadEntrega crearModalidad() {
        return new EntregaRetiro();
    }
}
