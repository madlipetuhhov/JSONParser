package org.example;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static java.lang.Double.parseDouble;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

public class JSONParser {
    public static final char END_OF_OBJECT = '}';
    private static final char END_OF_INPUT = Character.MAX_VALUE;
    public static final char NEXT_LINE = '\n';
    public static final char END_OF_ARRAY = ']';
    public static final char COMMA = ',';
    public static final char QUOTE = '"';
    public static final char START_OF_OBJECT = '{';
    public static final char START_OF_ARRAY = '[';
    public static final char MINUS = '-';
    public static final char DOT = '.';
    private char c;

    public Object parse(String input) {
        try (var reader = new StringReader(input)) {
            return new JSONParser().parse(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(Reader input) throws IOException {
        while (getNextChar(input) != END_OF_INPUT) {
            if (isWhitespace(c)) continue;
            else if (c == END_OF_ARRAY) return emptyList();
            else if (c == START_OF_OBJECT) return readObject(input);
            else if (c == START_OF_ARRAY) return readArray(input);
            else if (c == QUOTE) return readString(input);
            else if (isDigit(c) || c == MINUS) return readNumber(input, c);
            else if (c == 'n') return readNull(input);
            else if (Character.isAlphabetic(c)) return readBoolean(input, c);
            else throw new IllegalArgumentException("Unexpected character " + c);
        }
        throw new IllegalArgumentException("Unexpected end");
    }

    private String readString(Reader input) throws IOException {
        var string = new StringBuilder();
        while (getNextChar(input) != END_OF_INPUT) {
            if (isWhitespace(c) && string.isEmpty()) continue;
            boolean isFirstQuoteInObjectKey = c == QUOTE && string.isEmpty();
            if (isFirstQuoteInObjectKey || c == NEXT_LINE) continue;
            else {
                if (c == QUOTE) {
                    getNextChar(input);
                    return string.toString();
                }
            }
            string.append(c);
        }
        throw new IllegalArgumentException("Invalid end of string");
    }

    private Number readNumber(Reader input, char firstNumber) throws IOException {
        var string = new StringBuilder();
        string.append(firstNumber);
        while (getNextChar(input) != END_OF_INPUT) {
            if (c == NEXT_LINE) continue;
            if (c == COMMA || c == END_OF_ARRAY || c == END_OF_OBJECT) break;
            if (!isDigit(c) && c != DOT && c != MINUS) throw new IllegalArgumentException("Not a number");
            string.append(c);
        }
        if (string.toString().contains(".")) {
            return parseDouble(string.toString());
        } else {
            return Integer.parseInt(string.toString());
        }
    }

    private Object readNull(Reader input) throws IOException {
        if (isEqualTo(input, 3, "ull")) return null;
        throw new IllegalArgumentException("Not a null");
    }

    private boolean readBoolean(Reader input, char firstLetter) throws IOException {
        if (firstLetter == 't') {
            if (isEqualTo(input, 3, "rue")) return true;
        }
        if (firstLetter == 'f') {
            if (isEqualTo(input, 4, "alse")) return false;
        }
        throw new IllegalArgumentException("Not a boolean");
    }

    private List<Object> readArray(Reader input) throws IOException {
        var list = new ArrayList<>();
        while (c != END_OF_ARRAY) {
            var parser = parse(input);
            if (parser == emptyList()) {
                break;
            } else list.add(parser);
            if (c == COMMA) continue;
        }
        if (c != END_OF_ARRAY && c == END_OF_INPUT) throw new IllegalArgumentException("Invalid end of array");
        getNextChar(input);
        return list;
    }

    private Map<String, Object> readObject(Reader input) throws IOException {
        var map = new LinkedHashMap<String, Object>();
        while (c == COMMA || map.isEmpty()) {
            getNextChar(input);
            if (c == END_OF_OBJECT) return emptyMap();
            map.put(readString(input), parse(input));
        }
        if (c != END_OF_OBJECT && c == END_OF_INPUT) throw new IllegalArgumentException("Invalid end of object");
        getNextChar(input);
        return map;
    }

    private char getNextChar(Reader input) throws IOException {
        return c = (char) input.read();
    }

    private boolean isEqualTo(Reader input, int numOfChars, String equals) throws IOException {
        var buf = new char[numOfChars];
        input.read(buf);
        if (new String(buf).equals(equals)) {
            getNextChar(input);
            return true;
        }
        return false;
    }
}
