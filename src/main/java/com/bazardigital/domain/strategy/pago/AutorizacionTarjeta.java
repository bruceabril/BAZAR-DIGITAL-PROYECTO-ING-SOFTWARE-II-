package com.bazardigital.domain.strategy.pago;

public final class AutorizacionTarjeta implements DatosPago {
    private final String codigo;
    private final boolean aprobada;

    public AutorizacionTarjeta(String codigo, boolean aprobada) {
        this.codigo = codigo;
        this.aprobada = aprobada;
    }

    public String getCodigo() {
        return codigo;
    }

    public boolean estaAprobada() {
        return aprobada;
    }
}
