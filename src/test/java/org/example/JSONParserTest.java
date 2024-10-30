package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JSONParserTest {
    JSONParser parser = new JSONParser();

//    @Test
//    void empty() {
//        assertEquals(new IllegalArgumentException("Unexpected end"), parser.parse(""));
//    }

    @Test
    void string() {
        assertEquals("hello", parser.parse("\"hello\""));
    }

    @Test
    void integer() {
        assertEquals(123, parser.parse("123"));
    }

    @Test
    void minusNumber() {
        assertEquals(-123, parser.parse("-123"));
        assertEquals(-12.3, parser.parse("-12.3"));
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

    @Test
    void integerArray() {
        var expected = Arrays.asList(1, 22, 3);
        assertEquals(expected, parser.parse("[1, 22, 3]"));
    }

    @Test
    void minusNumberArray() {
        var expected = Arrays.asList(-1, 22.3, 3);
        assertEquals(expected, parser.parse("[-1, 22.3, 3]"));
    }

    @Test
    void doubleArray() {
        var expected = Arrays.asList(1.2, 22.2, 3.33);
        assertEquals(expected, parser.parse("[1.2, 22.2, 3.33]"));
    }

    @Test
    void emptyObject() {
        assertEquals(emptyMap(), parser.parse("{}"));
    }

    @Test
    void stringObject() {
        var expected = new LinkedHashMap<String, Object>();
        expected.put("key", "value");
        assertEquals(expected, parser.parse("{\"key\": \"value\"}"));
    }

    @Test
    void integerObject() {
        var expected = new LinkedHashMap<String, Object>();
        expected.put("key", 8);
        assertEquals(expected, parser.parse("{\"key\": 8}"));
    }

    @Test
    void nullObject() {
        var expected = new LinkedHashMap<String, Object>();
        expected.put("key", null);
        assertEquals(expected, parser.parse("{\"key\": null}"));
    }

    @Test
    void doubleObject() {
        var expected = new LinkedHashMap<String, Object>();
        expected.put("key", 8.8);
        assertEquals(expected, parser.parse("{\"key\": 8.8}"));
    }

    @Test
    void booleanObject() {
        var expected = new LinkedHashMap<String, Object>();
        expected.put("key", true);
        assertEquals(expected, parser.parse("{\"key\": true}"));
    }

    @Test
    void arrayObject() {
        var expected = new LinkedHashMap<String, Object>();
        expected.put("key", Arrays.asList("apple", "orange", "cherry"));
        assertEquals(expected, parser.parse("{\"key\": [\"apple\", \"orange\", \"cherry\"]}"));
    }

    @Test
    void unexpectedCharacterException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parser.parse("$"),
                "Unexpected character ");
    }
    //todo:  object inside of an object
    // todo: different types inside of an object

//    @Test
//    void arrayObject() {
//        var expected = new LinkedHashMap<String, Object>();
//        expected.put("key", Arrays.asList("apple", "orange", "cherry"));
//        assertEquals(expected, parser.parse("{\"key\": [\"apple\", \"orange\", \"cherry\"]}"));
//    }

}