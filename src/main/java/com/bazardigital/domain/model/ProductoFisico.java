package com.bazardigital.domain.model;

public class ProductoFisico extends Producto {
    private final String talla;
    private final String marca;

    public ProductoFisico(String id, String nombre, String descripcion, double precio,
                          int stock, Categoria categoria, String talla, String marca) {
        super(id, nombre, descripcion, precio, categoria, stock);
        this.talla = talla == null ? "" : talla;
        this.marca = marca == null ? "" : marca;
    }

    public String getTalla() {
        return talla;
    }

    public String getMarca() {
        return marca;
    }
}
