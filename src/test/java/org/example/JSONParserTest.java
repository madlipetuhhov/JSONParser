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
// todo: ] jouab parse meetodisse ja unexpected character
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

//    todo: } jouab readStringi
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
    void multipleNestedObjects() {
        var expected = new LinkedHashMap<String, Object>();
        var key11 = new LinkedHashMap<String, Object>();
        var key22 = new LinkedHashMap<String, Object>();
        var key33 = new LinkedHashMap<String, Object>();
        key33.put("key33", false);
        key11.put("key11", key33);
        key22.put("key22", Arrays.asList(1.2, 22.2, 3.33));
        expected.put("key1", key11);
        expected.put("key2", key22);
        expected.put("key3", null);
        expected.put("key4", "apple");
        expected.put("key5", 101);
//        todo: kas vaja muuta actuali
        assertEquals(expected, parser.parse("""
                {
                  "key1": {"key11": {"key33": false}},
                  "key2": {"key22": [1.2, 22.2, 3.33]},
                  "key3": null,
                  "key4": "apple",
                  "key5": 101
                }"""));
    }

    @Test
    void multipleObjects() {
        var expected = new LinkedHashMap<String, Object>();
        expected.put("key1", true);
        expected.put("key2", false);
        expected.put("key3", null);
        expected.put("key4", "apple");
        expected.put("key5", 101);
        assertEquals(expected, parser.parse("""
                {
                  "key1": true,
                  "key2": false,
                  "key3": null,
                  "key4": "apple",
                  "key5": 101
                }"""));
    }

//    @Test
//    void unexpectedCharacterException() {
//        Assertions.assertThrows(IllegalArgumentException.class,
//                () -> parser.parse("$"),
//                "Unexpected character ");
//    }
    //todo:  object inside of an object
    // todo: different types inside of an object
}