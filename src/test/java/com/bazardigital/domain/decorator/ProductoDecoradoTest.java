package com.bazardigital.domain.decorator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.bazardigital.domain.factory.FabricaProductoFisico;
import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.model.Producto;

class ProductoDecoradoTest {
    @Test
    void extrasSeAcumulanEnPrecioDescripcionYDeleganStock() {
        Producto base = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("R100", "Camisa", "Camisa de algodon", 50000, 4);
        Producto decorado = new EmpaqueRegalo(new GarantiaExtendida(base, 8000), 2000);

        assertEquals(60000, decorado.getPrecio());
        assertTrue(decorado.getDescripcion().contains("Garantia extendida"));
        assertTrue(decorado.getDescripcion().contains("Empaque de regalo"));
        decorado.actualizarStock(3);
        assertEquals(3, base.getStock());
    }

    @Test
    void extraNoAceptaCostoNegativo() {
        Producto base = new FabricaProductoFisico(Categoria.ROPA)
                .crearProducto("R101", "Camisa", "Camisa", 50000, 4);

        assertThrows(IllegalArgumentException.class, () -> new EnvioExpress(base, -1));
    }
}
