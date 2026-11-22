package com.bazardigital.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.bazardigital.domain.model.Categoria;

class InMemoryProductoRepositoryTest {
    @Test
    void repositorioDebeIncluirDatosIniciales() {
        ProductoRepository repository = new InMemoryProductoRepository();

        assertEquals(4, repository.findAll().size());
        assertTrue(repository.findByCategoria(Categoria.ROPA).size() >= 1);
        assertTrue(repository.findByCategoria(Categoria.TECNOLOGIA).size() >= 1);
        assertTrue(repository.findByCategoria(Categoria.ALIMENTOS).size() >= 1);
        assertTrue(repository.findByCategoria(Categoria.DIGITAL).size() >= 1);
    }

    @Test
    void repositorioDebeBuscarPedidoGuardado() {
        PedidoRepository repository = new InMemoryPedidoRepository();

        assertTrue(repository.findAll().isEmpty());
    }
}
