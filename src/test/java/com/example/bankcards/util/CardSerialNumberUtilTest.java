package com.example.bankcards.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardSerialNumberUtilTest {

    @Test
    void testGenerate() {
        String code = CardSerialNumberUtil.generate(2, 4);
        assertEquals(9, code.length());
    }

    @Test
    void testEncodeSerialNumber() {
        String serial = "1111 2222 3333 4444";
        String encodedSerialResult = "**** **** **** 4444";

        String testData = CardSerialNumberUtil.encodeSerialNumber(serial);

        assertEquals(encodedSerialResult, testData);
    }
}