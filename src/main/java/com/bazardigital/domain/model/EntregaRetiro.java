package com.bazardigital.domain.model;

public class EntregaRetiro implements ModalidadEntrega {
    @Override
    public String getNombre() {
        return "Retiro en punto del bazar";
    }

    @Override
    public double getCostoAdicional() {
        return 0;
    }
}
