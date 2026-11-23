package com.bazardigital.domain.strategy.descuento;

import java.util.Objects;

public class DescuentoCupon implements EstrategiaDescuento {
    private final String codigoValido;
    private final String codigoIngresado;
    private final double porcentaje;
    private final boolean acumulable;

    public DescuentoCupon(String codigoValido, String codigoIngresado, double porcentaje) {
        this(codigoValido, codigoIngresado, porcentaje, false);
    }

    public DescuentoCupon(String codigoValido, String codigoIngresado, double porcentaje,
                          boolean acumulable) {
        this.codigoValido = validarCodigo(codigoValido);
        this.codigoIngresado = validarCodigo(codigoIngresado);
        DescuentoPorCantidad.validarPorcentaje(porcentaje);
        this.porcentaje = porcentaje;
        this.acumulable = acumulable;
    }

    private static String validarCodigo(String codigo) {
        Objects.requireNonNull(codigo, "El codigo del cupon es obligatorio");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo del cupon es obligatorio");
        }
        return codigo.trim();
    }

    @Override
    public String getNombre() {
        return "Descuento por cupon";
    }

    @Override
    public boolean esAcumulable() {
        return acumulable;
    }

    @Override
    public double calcularDescuento(double subtotal, int cantidadArticulos) {
        return codigoValido.equalsIgnoreCase(codigoIngresado)
                ? subtotal * porcentaje / 100 : 0;
    }
}
