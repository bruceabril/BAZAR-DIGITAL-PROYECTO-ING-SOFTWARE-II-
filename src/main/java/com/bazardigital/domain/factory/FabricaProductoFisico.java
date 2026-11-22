package com.bazardigital.domain.factory;

import com.bazardigital.domain.model.Categoria;
import com.bazardigital.domain.model.Producto;
import com.bazardigital.domain.model.ProductoFisico;

public class FabricaProductoFisico extends FabricaProducto {
    private final Categoria categoria;
    private final String talla;
    private final String marca;

    public FabricaProductoFisico(Categoria categoria) {
        this(categoria, "", "");
    }

    public FabricaProductoFisico(Categoria categoria, String talla, String marca) {
        if (categoria == Categoria.DIGITAL || categoria == Categoria.COMBOS) {
            throw new IllegalArgumentException("Categoria incompatible con un producto fisico");
        }
        this.categoria = categoria;
        this.talla = talla;
        this.marca = marca;
    }

    @Override
    public Producto crearProducto(String id, String nombre, String descripcion,
                                  double precio, int stock) {
        return new ProductoFisico(id, nombre, descripcion, precio, stock,
                categoria, talla, marca);
    }
}
