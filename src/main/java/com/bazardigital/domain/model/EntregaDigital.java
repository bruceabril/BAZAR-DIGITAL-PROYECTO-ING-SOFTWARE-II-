package com.bazardigital.domain.model;

public class EntregaDigital implements ModalidadEntrega {
    @Override
    public String getNombre() {
        return "Entrega digital";
    }

    @Override
    public double getCostoAdicional() {
        return 0;
    }
}
