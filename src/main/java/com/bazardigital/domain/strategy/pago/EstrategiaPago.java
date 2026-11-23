package com.bazardigital.domain.strategy.pago;

public interface EstrategiaPago {
    String getNombre();

    ResultadoPago procesar(double monto, DatosPago datosPago);
}
