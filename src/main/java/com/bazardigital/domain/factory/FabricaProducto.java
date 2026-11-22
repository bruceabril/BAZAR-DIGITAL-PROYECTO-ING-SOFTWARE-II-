package com.bazardigital.domain.factory;

import com.bazardigital.domain.model.Producto;

public abstract class FabricaProducto {
    public abstract Producto crearProducto(String id, String nombre, String descripcion,
                                           double precio, int stock);
}
