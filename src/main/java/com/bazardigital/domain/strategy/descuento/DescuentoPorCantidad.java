package com.bazardigital.domain.strategy.descuento;

public class DescuentoPorCantidad implements EstrategiaDescuento {
    private final int cantidadMinima;
    private final double porcentaje;
    private final boolean acumulable;

    public DescuentoPorCantidad(int cantidadMinima, double porcentaje) {
        this(cantidadMinima, porcentaje, true);
    }

    public DescuentoPorCantidad(int cantidadMinima, double porcentaje, boolean acumulable) {
        if (cantidadMinima <= 0) {
            throw new IllegalArgumentException("La cantidad minima debe ser mayor que cero");
        }
        validarPorcentaje(porcentaje);
        this.cantidadMinima = cantidadMinima;
        this.porcentaje = porcentaje;
        this.acumulable = acumulable;
    }

    @Override
    public String getNombre() {
        return "Descuento por cantidad";
    }

    @Override
    public boolean esAcumulable() {
        return acumulable;
    }

    @Override
    public double calcularDescuento(double subtotal, int cantidadArticulos) {
        return cantidadArticulos >= cantidadMinima ? subtotal * porcentaje / 100 : 0;
    }

    static void validarPorcentaje(double porcentaje) {
        if (!Double.isFinite(porcentaje) || porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
    }
}
