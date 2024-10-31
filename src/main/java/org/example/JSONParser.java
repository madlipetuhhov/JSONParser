package org.example;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class JSONParser {
    private char c;

    public Object parse(String input) {
        try {
            return parse(new StringReader(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(Reader input) throws IOException {
        while (nextChar(input) != endOfStringReader()) {
            if (Character.isWhitespace(c)) continue;
            else if (isEndOfArray()) return emptyList();
            else if (c == '{') return readObject(input);
            else if (c == '[') return readArray(input);
            else if (isQuote()) return readString(input);
            else if (Character.isDigit(c) || c == '-') return readNumber(input, c);
            else if (c == 'n') return readNull(input);
            else if (Character.isAlphabetic(c)) return readBoolean(input, c);
            else throw new IllegalArgumentException("Unexpected character " + c);
        }
        throw new IllegalArgumentException("Unexpected end");
    }

    private String readString(Reader input) throws IOException {
        var string = new StringBuilder();
        while (nextChar(input) != endOfStringReader()) {
            if (Character.isWhitespace(c) && string.isEmpty()) continue;
            boolean isFirstQuoteInObjectKey = isQuote() && string.isEmpty();
            if (isFirstQuoteInObjectKey || isNextLine()) continue;
            else {
                if (isQuote()) {
                    nextChar(input);
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
        while (nextChar(input) != endOfStringReader()) {
            if (isNextLine()) continue;
            if (isComma() || isEndOfArray() || isEndOfObject()) break;
            if (!Character.isDigit(c) && c != '.' && c != '-') throw new IllegalArgumentException("Not number");
            string.append(c);
        }
        if (string.toString().contains(".")) {
            return Double.parseDouble(string.toString());
        } else return Integer.parseInt(string.toString());
    }

    private Object readNull(Reader input) throws IOException {
        var buf = new char[3];
        input.read(buf);
        if (new String(buf).equals("ull")) {
            nextChar(input);
            return null;
        }
        throw new IllegalArgumentException("Unexpected input");
    }

    private boolean readBoolean(Reader input, char firstLetter) throws IOException {
        if (firstLetter == 't') {
            var buf = new char[3];
            input.read(buf);
            if (new String(buf).equals("rue")) {
                nextChar(input);
                return true;
            }
        }
        if (firstLetter == 'f') {
            var buf = new char[4];
            input.read(buf);
            if (new String(buf).equals("alse")) {
                nextChar(input);
                return false;
            }
        }
        throw new IllegalArgumentException("Unexpected input");
    }

    private List<Object> readArray(Reader input) throws IOException {
        var list = new ArrayList<>();
        while (c != ']') {
            var parser = parse(input);
            if (parser == emptyList()) {
                break;
            } else list.add(parser);
            if (isComma()) continue;
        }
        nextChar(input);
        return list;
//        throw new IllegalArgumentException("Unexpected input");
    }

    private Map<String, Object> readObject(Reader input) throws IOException {
        var map = new LinkedHashMap<String, Object>();
        while (isComma() || map.isEmpty()) {
            map.put(readString(input), parse(input));
        }
        nextChar(input);
        return map;
//        return emptyMap();
    }

    private boolean isQuote() {
        return c == '"';
    }

    private boolean isComma() {
        return c == ',';
    }

    private boolean isEndOfArray() {
        return c == ']';
    }

    private boolean isEndOfObject() {
        return c == '}';
    }

    private boolean isNextLine() {
        return c == '\n';
    }

    private static char endOfStringReader() {
        return Character.MAX_VALUE;
    }

    private char nextChar(Reader input) throws IOException {
        return c = (char) input.read();
    }
}
