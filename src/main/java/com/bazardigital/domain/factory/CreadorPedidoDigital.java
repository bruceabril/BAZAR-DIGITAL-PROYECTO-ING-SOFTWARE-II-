package com.bazardigital.domain.factory;

import com.bazardigital.domain.model.EntregaDigital;
import com.bazardigital.domain.model.ModalidadEntrega;

public class CreadorPedidoDigital extends CreadorPedido {
    @Override
    protected ModalidadEntrega crearModalidad() {
        return new EntregaDigital();
    }
}
