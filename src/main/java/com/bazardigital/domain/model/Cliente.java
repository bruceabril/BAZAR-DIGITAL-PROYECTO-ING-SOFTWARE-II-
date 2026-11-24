package com.bazardigital.domain.model;

import java.util.Objects;

public class Cliente {
    private final String id;
    private final String nombre;
    private final String correo;

    public Cliente(String id, String nombre, String correo) {
        this.id = validarTexto(id, "El id del cliente es obligatorio");
        this.nombre = validarTexto(nombre, "El nombre del cliente es obligatorio");
        this.correo = validarTexto(correo, "El correo del cliente es obligatorio");
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

    private static String validarTexto(String valor, String mensaje) {
        String texto = Objects.requireNonNull(valor, mensaje).trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
        return texto;
    }
}
