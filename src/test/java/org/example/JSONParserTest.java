package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JSONParserTest {
    JSONParser parser = new JSONParser();

    @Test
    void emptyObject() {
        assertEquals(emptyMap(), parser.parse("{}"));
    }

    @Test
    void string() {
        assertEquals("hello", parser.parse("\"hello\""));
    }

    @Test
    void integer() {
        assertEquals(123, parser.parse("123"));
    }

    @Test
    void doubleNumber() {
        assertEquals(1.23, parser.parse("1.23"));
    }

    @Test
    void nullValue() {
        assertNull(parser.parse("null"));
    }

    @Test
    void booleanValue() {
        assertEquals(true, parser.parse("true"));
        assertEquals(false, parser.parse("false"));
    }

    @Test
    void emptyArray() {
        assertEquals(emptyList(), parser.parse("[]"));
    }

    @Test
    void stringArray() {
        var expected = Arrays.asList("apple", "orange", "cherry");
        assertEquals(expected, parser.parse("[\"apple\", \"orange\", \"cherry\"]"));
    }
}