package com.bazardigital.domain.strategy.descuento;

public class DescuentoClienteFrecuente implements EstrategiaDescuento {
    private final boolean clienteFrecuente;
    private final double porcentaje;
    private final boolean acumulable;

    public DescuentoClienteFrecuente(boolean clienteFrecuente, double porcentaje) {
        this(clienteFrecuente, porcentaje, true);
    }

    public DescuentoClienteFrecuente(boolean clienteFrecuente, double porcentaje,
                                     boolean acumulable) {
        DescuentoPorCantidad.validarPorcentaje(porcentaje);
        this.clienteFrecuente = clienteFrecuente;
        this.porcentaje = porcentaje;
        this.acumulable = acumulable;
    }

    @Override
    public String getNombre() {
        return "Descuento de cliente frecuente";
    }

    @Override
    public boolean esAcumulable() {
        return acumulable;
    }

    @Override
    public double calcularDescuento(double subtotal, int cantidadArticulos) {
        return clienteFrecuente ? subtotal * porcentaje / 100 : 0;
    }
}
