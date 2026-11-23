package com.bazardigital.domain.strategy.pago;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EstrategiasPagoTest {
    @Test
    void transferenciaRequiereReferenciaYLaConservaEnElResultado() {
        PagoTransferencia transferencia = new PagoTransferencia();

        assertThrows(IllegalArgumentException.class, () -> transferencia.procesar(5000, null));
        ResultadoPago resultado = transferencia.procesar(5000,
                new ReferenciaTransferencia("  REF-123  "));

        assertEquals("REF-123", resultado.getReferencia());
        assertEquals(5000, resultado.getMonto());
    }

    @Test
    void tarjetaSoloAceptaAutorizacionAprobada() {
        PagoTarjeta tarjeta = new PagoTarjeta();

        assertThrows(IllegalArgumentException.class, () -> tarjeta.procesar(5000,
                new AutorizacionTarjeta("AUTH-1", false)));
        assertEquals("AUTH-2", tarjeta.procesar(5000,
                new AutorizacionTarjeta("AUTH-2", true)).getReferencia());
    }
}
