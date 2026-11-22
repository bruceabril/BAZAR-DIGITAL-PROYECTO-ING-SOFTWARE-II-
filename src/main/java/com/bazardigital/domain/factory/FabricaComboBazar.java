package com.bazardigital.domain.factory;

import java.util.Collections;
import java.util.List;

import com.bazardigital.domain.model.ComboBazar;
import com.bazardigital.domain.model.Producto;

public class FabricaComboBazar extends FabricaProducto {
    private final List<Producto> productosIncluidos;

    public FabricaComboBazar() {
        this(Collections.emptyList());
    }

    public FabricaComboBazar(List<Producto> productosIncluidos) {
        this.productosIncluidos = productosIncluidos == null
                ? Collections.emptyList()
                : productosIncluidos;
    }

    @Override
    public Producto crearProducto(String id, String nombre, String descripcion,
                                  double precio, int stock) {
        return new ComboBazar(id, nombre, descripcion, precio, stock, productosIncluidos);
    }
}
