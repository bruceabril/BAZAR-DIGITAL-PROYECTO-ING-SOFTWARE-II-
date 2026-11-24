package com.bazardigital.domain.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.bazardigital.domain.factory.FabricaProductoFisico;

class ValidacionDominioTest {
    @Test
    void productoRechazaPreciosNoFinitos() {
        FabricaProductoFisico fabrica = new FabricaProductoFisico(Categoria.ROPA);

        assertThrows(IllegalArgumentException.class,
                () -> fabrica.crearProducto("P-1", "Producto", "", Double.NaN, 1));
        assertThrows(IllegalArgumentException.class,
                () -> fabrica.crearProducto("P-2", "Producto", "", Double.POSITIVE_INFINITY, 1));
    }

    @Test
    void entregaDomicilioRechazaCostosNegativosONoFinitos() {
        assertThrows(IllegalArgumentException.class, () -> new EntregaDomicilio(-1));
        assertThrows(IllegalArgumentException.class, () -> new EntregaDomicilio(Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> new EntregaDomicilio(Double.POSITIVE_INFINITY));
    }

    @Test
    void clienteRechazaCamposObligatoriosVacios() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente("C-1", " ", "ana@example.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente("C-1", "Ana", ""));
    }
}
