package com.aguaalerta.agua_alerta.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DenunciaTest {

    @Test
    void preparaValoresPadraoAntesDePersistir() {
        Denuncia denuncia = new Denuncia();

        denuncia.prepararDados();

        assertNotNull(denuncia.getData());
        assertEquals(StatusDenuncia.PENDENTE, denuncia.consultarStatus());
        assertNotNull(denuncia.getFotos());
        assertTrue(denuncia.getFotos().isEmpty());
    }
}
