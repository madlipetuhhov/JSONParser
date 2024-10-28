package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    JSONParser parser = new JSONParser();

    @Test
    void emptyObject() {
        assertEquals(new JSONObject(), parser.parser("{}"));
    }
}