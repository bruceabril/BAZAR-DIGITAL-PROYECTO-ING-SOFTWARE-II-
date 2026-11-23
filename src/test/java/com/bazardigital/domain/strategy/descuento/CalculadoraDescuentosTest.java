package com.bazardigital.domain.strategy.descuento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class CalculadoraDescuentosTest {
    @Test
    void acumulaDescuentosAplicablesSobreElSubtotalOriginal() {
        ResultadoDescuento resultado = CalculadoraDescuentos.calcular(100000, 3, List.of(
                new DescuentoPorCantidad(3, 10),
                new DescuentoTemporada(5),
                new DescuentoClienteFrecuente(true, 2),
                new DescuentoCupon("BAZAR", "Bazar", 10)));

        assertEquals(17000, resultado.getMonto());
        assertEquals(3, resultado.getDescuentosAplicados().size());
    }

    @Test
    void mejorDescuentoExclusivoSeComparaConLaSumaAcumulable() {
        ResultadoDescuento resultado = CalculadoraDescuentos.calcular(100000, 3, List.of(
                new DescuentoPorCantidad(3, 10),
                new DescuentoTemporada(5),
                new DescuentoClienteFrecuente(true, 2),
                new DescuentoCupon("BAZAR", "Bazar", 20)));

        assertEquals(20000, resultado.getMonto());
        assertEquals(List.of("Descuento por cupon"), resultado.getDescuentosAplicados());
    }

    @Test
    void descuentosAcumulablesNoSuperanElSubtotal() {
        ResultadoDescuento resultado = CalculadoraDescuentos.calcular(100, 1, List.of(
                new DescuentoTemporada(80),
                new DescuentoClienteFrecuente(true, 80)));

        assertEquals(100, resultado.getMonto());
        assertEquals(2, resultado.getDescuentosAplicados().size());
    }

    @Test
    void cantidadMinimaYCodigoDeCuponSeValidanAntesDeAplicar() {
        ResultadoDescuento resultado = CalculadoraDescuentos.calcular(1000, 1, List.of(
                new DescuentoPorCantidad(2, 10),
                new DescuentoCupon("VALIDO", "INVALIDO", 50)));

        assertEquals(0, resultado.getMonto());
        assertTrue(resultado.getDescuentosAplicados().isEmpty());
    }
}
