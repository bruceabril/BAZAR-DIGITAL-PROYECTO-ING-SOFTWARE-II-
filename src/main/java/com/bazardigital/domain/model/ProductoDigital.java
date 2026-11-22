package com.bazardigital.domain.model;

public class ProductoDigital extends Producto {
    private final String formato;
    private final String enlaceDescarga;

    public ProductoDigital(String id, String nombre, String descripcion, double precio,
                           int stock, String formato, String enlaceDescarga) {
        super(id, nombre, descripcion, precio, Categoria.DIGITAL, stock);
        this.formato = formato == null ? "" : formato;
        this.enlaceDescarga = enlaceDescarga == null ? "" : enlaceDescarga;
    }

    public String getFormato() {
        return formato;
    }

    public String getEnlaceDescarga() {
        return enlaceDescarga;
    }
}
