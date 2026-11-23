package com.bazardigital.domain.strategy.descuento;

import java.util.List;

public final class ResultadoDescuento {
    private final double monto;
    private final List<String> descuentosAplicados;

    public ResultadoDescuento(double monto, List<String> descuentosAplicados) {
        this.monto = monto;
        this.descuentosAplicados = List.copyOf(descuentosAplicados);
    }

    public double getMonto() {
        return monto;
    }

    public List<String> getDescuentosAplicados() {
        return descuentosAplicados;
    }
}
