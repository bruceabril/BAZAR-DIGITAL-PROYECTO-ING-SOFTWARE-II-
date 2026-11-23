package com.bazardigital.domain.strategy.descuento;

public interface EstrategiaDescuento {
    String getNombre();

    boolean esAcumulable();

    double calcularDescuento(double subtotal, int cantidadArticulos);
}
