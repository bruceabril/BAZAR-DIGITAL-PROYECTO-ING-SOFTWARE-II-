package com.bazardigital.domain.strategy.pago;

public final class ReferenciaTransferencia implements DatosPago {
    private final String referencia;

    public ReferenciaTransferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }
}
