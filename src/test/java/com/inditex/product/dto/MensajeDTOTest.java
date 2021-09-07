package com.inditex.product.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MensajeDTOTest {

    @Test
    void getMensaje() {
        MessageDTO messageDTO = new MessageDTO("test");
        messageDTO.setMensaje("test");
        assertEquals("test", messageDTO.getMensaje());
    }
}