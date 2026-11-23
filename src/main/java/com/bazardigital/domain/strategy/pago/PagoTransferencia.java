package com.bazardigital.domain.strategy.pago;

public class PagoTransferencia implements EstrategiaPago {
    @Override
    public String getNombre() {
        return "Transferencia";
    }

    @Override
    public ResultadoPago procesar(double monto, DatosPago datosPago) {
        PagoTarjeta.validarMonto(monto);
        if (!(datosPago instanceof ReferenciaTransferencia transferencia)
                || transferencia.getReferencia() == null
                || transferencia.getReferencia().isBlank()) {
            throw new IllegalArgumentException("La transferencia requiere una referencia");
        }
        return new ResultadoPago(transferencia.getReferencia().trim(), getNombre(), monto);
    }
}
