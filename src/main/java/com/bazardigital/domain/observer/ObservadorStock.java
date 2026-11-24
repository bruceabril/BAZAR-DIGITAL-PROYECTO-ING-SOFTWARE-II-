package com.bazardigital.domain.observer;

import com.bazardigital.domain.model.Producto;

@FunctionalInterface
public interface ObservadorStock {
    void alCambiarStock(Producto producto, int stockAnterior, int stockNuevo);
}
