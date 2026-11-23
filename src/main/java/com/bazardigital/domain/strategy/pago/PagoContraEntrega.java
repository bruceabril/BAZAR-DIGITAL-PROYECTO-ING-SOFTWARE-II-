package com.bazardigital.domain.strategy.pago;

import java.util.UUID;

public class PagoContraEntrega implements EstrategiaPago {
    @Override
    public String getNombre() {
        return "Contra entrega";
    }

    @Override
    public ResultadoPago procesar(double monto, DatosPago datosPago) {
        PagoTarjeta.validarMonto(monto);
        return new ResultadoPago(UUID.randomUUID().toString(), getNombre(), monto);
    }
}
