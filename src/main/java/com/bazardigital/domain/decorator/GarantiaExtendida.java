package com.bazardigital.domain.decorator;

import com.bazardigital.domain.model.Producto;

public class GarantiaExtendida extends ProductoDecorado {
    public GarantiaExtendida(Producto producto, double costo) {
        super(producto, "Garantia extendida", costo);
    }
}
