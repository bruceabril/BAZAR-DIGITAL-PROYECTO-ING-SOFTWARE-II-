package com.bazardigital.domain.strategy.pago;

public class PagoTarjeta implements EstrategiaPago {
    @Override
    public String getNombre() {
        return "Tarjeta";
    }

    @Override
    public ResultadoPago procesar(double monto, DatosPago datosPago) {
        validarMonto(monto);
        if (!(datosPago instanceof AutorizacionTarjeta autorizacion)
                || autorizacion.getCodigo() == null || autorizacion.getCodigo().isBlank()
                || !autorizacion.estaAprobada()) {
            throw new IllegalArgumentException("La tarjeta requiere una autorizacion aprobada");
        }
        return new ResultadoPago(autorizacion.getCodigo(), getNombre(), monto);
    }

    static void validarMonto(double monto) {
        if (!Double.isFinite(monto) || monto < 0) {
            throw new IllegalArgumentException("El monto debe ser finito y no negativo");
        }
    }
}
