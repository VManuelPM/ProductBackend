package com.inditex.product.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MensajeDTOTest {

    @Test
    void getMensaje() {
        MensajeDTO mensajeDTO = new MensajeDTO("test");
        mensajeDTO.setMensaje("test");
        assertEquals("test", mensajeDTO.getMensaje());
    }
}