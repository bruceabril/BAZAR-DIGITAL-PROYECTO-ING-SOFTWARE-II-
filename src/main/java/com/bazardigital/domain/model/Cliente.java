package com.bazardigital.domain.model;

import java.util.Objects;

public class Cliente {
    private final String id;
    private final String nombre;
    private final String correo;

    public Cliente(String id, String nombre, String correo) {
        this.id = Objects.requireNonNull(id);
        this.nombre = Objects.requireNonNull(nombre);
        this.correo = Objects.requireNonNull(correo);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}
