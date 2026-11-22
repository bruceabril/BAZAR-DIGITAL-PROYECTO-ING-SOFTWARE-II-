package com.bazardigital.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComboBazar extends Producto {
    private final List<Producto> productos;

    public ComboBazar(String id, String nombre, String descripcion, double precio,
                      int stock, List<Producto> productos) {
        super(id, nombre, descripcion, precio, Categoria.COMBOS, stock);
        this.productos = new ArrayList<>(productos == null ? Collections.emptyList() : productos);
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }
}
