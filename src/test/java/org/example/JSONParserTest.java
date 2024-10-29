package org.example;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class JSONParserTest {
    JSONParser parser = new JSONParser();

    @Test
    void emptyObject() {
        assertEquals(emptyMap(), parser.parser("{}"));
    }

    @Test
    void string() {
        assertEquals("hello", parser.parser("""
                "hello"
                """));
    }

    @Test
    void integer() {
        assertEquals(123, parser.parser("123"));
    }
}