package com.bazardigital.domain.strategy.descuento;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CalculadoraDescuentos {
    private CalculadoraDescuentos() {
    }

    public static ResultadoDescuento calcular(double subtotal, int cantidadArticulos,
                                               List<EstrategiaDescuento> estrategias) {
        if (!Double.isFinite(subtotal) || subtotal < 0) {
            throw new IllegalArgumentException("El subtotal debe ser finito y no negativo");
        }
        if (cantidadArticulos < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        Objects.requireNonNull(estrategias, "Las estrategias de descuento son obligatorias");

        double descuentosAcumulables = 0;
        List<String> nombresAcumulables = new ArrayList<>();
        double mejorDescuentoExclusivo = 0;
        String nombreDescuentoExclusivo = null;

        for (EstrategiaDescuento estrategia : estrategias) {
            Objects.requireNonNull(estrategia, "La estrategia de descuento es obligatoria");
            double monto = estrategia.calcularDescuento(subtotal, cantidadArticulos);
            if (!Double.isFinite(monto) || monto < 0) {
                throw new IllegalArgumentException("La estrategia produjo un descuento invalido");
            }
            monto = Math.min(monto, subtotal);
            if (monto == 0) {
                continue;
            }
            if (estrategia.esAcumulable()) {
                descuentosAcumulables += monto;
                nombresAcumulables.add(estrategia.getNombre());
            } else if (monto > mejorDescuentoExclusivo) {
                mejorDescuentoExclusivo = monto;
                nombreDescuentoExclusivo = estrategia.getNombre();
            }
        }

        if (mejorDescuentoExclusivo > descuentosAcumulables) {
            return new ResultadoDescuento(mejorDescuentoExclusivo,
                    List.of(nombreDescuentoExclusivo));
        }
        return new ResultadoDescuento(Math.min(descuentosAcumulables, subtotal),
                nombresAcumulables);
    }
}
