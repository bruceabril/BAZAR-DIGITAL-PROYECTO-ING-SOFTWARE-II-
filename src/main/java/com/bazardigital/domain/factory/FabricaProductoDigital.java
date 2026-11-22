package com.bazardigital.domain.factory;

import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.model.ProductoDigital;

public class FabricaProductoDigital extends FabricaProducto {
    private final String formato;
    private final String enlaceDescarga;

    public FabricaProductoDigital() {
        this("PDF", "");
    }

    public FabricaProductoDigital(String formato, String enlaceDescarga) {
        this.formato = formato;
        this.enlaceDescarga = enlaceDescarga;
    }

    @Override
    public Producto crearProducto(String id, String nombre, String descripcion,
                                  double precio, int stock) {
        return new ProductoDigital(id, nombre, descripcion, precio, stock,
                formato, enlaceDescarga);
    }
}
