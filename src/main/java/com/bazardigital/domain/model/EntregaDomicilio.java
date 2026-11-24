package com.bazardigital.domain.model;

public class EntregaDomicilio implements ModalidadEntrega {
    private final double costoAdicional;

    public EntregaDomicilio() {
        this(10000);
    }

    public EntregaDomicilio(double costoAdicional) {
        if (!Double.isFinite(costoAdicional) || costoAdicional < 0) {
            throw new IllegalArgumentException("El costo de entrega debe ser finito y no negativo");
        }
        this.costoAdicional = costoAdicional;
    }

    @Override
    public String getNombre() {
        return "Envio a domicilio";
    }

    @Override
    public double getCostoAdicional() {
        return costoAdicional;
    }
}
