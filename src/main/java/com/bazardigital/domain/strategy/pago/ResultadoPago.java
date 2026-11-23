package com.bazardigital.domain.strategy.pago;

import java.util.Objects;

public final class ResultadoPago {
    private final String referencia;
    private final String medioPago;
    private final double monto;

    public ResultadoPago(String referencia, String medioPago, double monto) {
        this.referencia = Objects.requireNonNull(referencia);
        this.medioPago = Objects.requireNonNull(medioPago);
        this.monto = monto;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public double getMonto() {
        return monto;
    }
}
