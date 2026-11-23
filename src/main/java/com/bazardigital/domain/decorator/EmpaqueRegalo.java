package com.bazardigital.domain.decorator;

import com.bazardigital.domain.model.Producto;

public class EmpaqueRegalo extends ProductoDecorado {
    public EmpaqueRegalo(Producto producto, double costo) {
        super(producto, "Empaque de regalo", costo);
    }
}
