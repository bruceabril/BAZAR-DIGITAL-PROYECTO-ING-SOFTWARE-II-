package com.bazardigital.domain.decorator;

import com.bazardigital.domain.model.Producto;

public class EnvioExpress extends ProductoDecorado {
    public EnvioExpress(Producto producto, double costo) {
        super(producto, "Envio express", costo);
    }
}
