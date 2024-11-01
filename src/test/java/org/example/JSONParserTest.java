package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class JSONParserTest {
    JSONParser parser = new JSONParser();

    @Test
    void string() {
        assertEquals("hello, Mari & Toomas", parser.parse("\"hello, Mari & Toomas\""));
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
    void multipleNestedObjects() {
        var expected = Map.of(
                "key1", Map.of(
                        "key11", Map.of(
                                "key33", false
                        )
                ),
                "key2", Map.of(
                        "key22", List.of(1.2, 22.2, 3.33)
                ),
                "key3", true,
                "key4", "apple pie",
                "key5", 101
        );

        assertEquals(expected, parser.parse(/* language=json */ """
                {
                  "key1": {"key11": {"key33": false}},
                  "key2": {"key22": [1.2, 22.2, 3.33]},
                  "key3": true,
                  "key4": "apple pie",
                  "key5": 101
                }"""));
    }

    @Test
    void oneLineMultipleNestedObjects() {
        var expected = Map.of(
                "key1", Map.of(
                        "key2", Map.of(
                                "key3", false)));
        assertEquals(expected, parser.parse(/* language=json */ """
                {"key1": {"key2": {"key3": false}}}"""));
    }

    @Test
    void multipleObjects() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key1", true);
            put("key2", false);
            put("key3", null);
            put("key4", "apple pie");
            put("key5", 101);
        }};
        assertEquals(expected, parser.parse(/* language=json */ """
                {
                  "key1": true,
                  "key2": false,
                  "key3": null,
                  "key4": "apple pie",
                  "key5": 101
                }"""));
    }

    @Test
    void unexpectedCharacterException() {
        assertEquals("Unexpected character $",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("$")).getMessage());
    }

    @Test
    void unexpectedEndException() {
        assertEquals("Unexpected end",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("")).getMessage());
    }

    @Test
    void invalidEndOfStringException() {
        assertEquals("Invalid end of string",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("\"jah")).getMessage());
    }

    @Test
    void notANumberException() {
        assertEquals("Not a number",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("8$")).getMessage());
    }

    @Test
    void notNullException() {
        assertEquals("Not a null",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("nuul")).getMessage());
    }

    @Test
    void notTrueException() {
        assertEquals("Not a boolean",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("rue")).getMessage());
    }

    @Test
    void notFalseException() {
        assertEquals("Not a boolean",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("fal")).getMessage());
    }

    @Test
    void invalidEndOfArrayException() {
        assertEquals("Unexpected end",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("[1, 22, 3")).getMessage());
    }

    @Test
    void invalidEndOfObjectException() {
        assertEquals("Invalid end of object",
                assertThrows(IllegalArgumentException.class, () ->
                        parser.parse("{\"key\": true")).getMessage());
    }
}