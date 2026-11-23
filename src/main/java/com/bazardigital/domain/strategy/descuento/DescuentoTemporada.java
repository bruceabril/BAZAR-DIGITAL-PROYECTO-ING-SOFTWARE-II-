package com.bazardigital.domain.strategy.descuento;

public class DescuentoTemporada implements EstrategiaDescuento {
    private final double porcentaje;
    private final boolean acumulable;

    public DescuentoTemporada(double porcentaje) {
        this(porcentaje, true);
    }

    public DescuentoTemporada(double porcentaje, boolean acumulable) {
        DescuentoPorCantidad.validarPorcentaje(porcentaje);
        this.porcentaje = porcentaje;
        this.acumulable = acumulable;
    }

    @Override
    public String getNombre() {
        return "Descuento de temporada";
    }

    @Override
    public boolean esAcumulable() {
        return acumulable;
    }

    @Override
    public double calcularDescuento(double subtotal, int cantidadArticulos) {
        return subtotal * porcentaje / 100;
    }
}
