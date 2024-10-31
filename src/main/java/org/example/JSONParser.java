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
        while ((c = (char) input.read()) != Character.MAX_VALUE) {
            if (Character.isWhitespace(c)) continue;
//            todo: edasi emptyListiga
            else if (c == ']') return emptyList();
                //else if (c == ':' || c == ',') return parse(input);
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
        while ((c = (char) input.read()) != Character.MAX_VALUE) {
            if (Character.isWhitespace(c) && string.isEmpty()) continue;
            boolean isFirstQuoteInObjectKey = isQuote() && string.isEmpty();
            if (isFirstQuoteInObjectKey || c == '\n') continue;
            else {
                if (isQuote()) {
                    c = (char) input.read();
                    return string.toString();
                }
            }
            string.append(c);
        }
        throw new IllegalArgumentException("Invalid end of string");
    }

    private boolean isQuote() {
        return c == '"';
    }

    private Number readNumber(Reader input, char firstNumber) throws IOException {
        var string = new StringBuilder();
        string.append(firstNumber);
        while ((c = (char) input.read()) != Character.MAX_VALUE) {
            if (!Character.isDigit(c) && c != '.' && c != '-') break;
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
            c = (char) input.read();
            return null;
        }
        throw new IllegalArgumentException("Unexpected input");
    }

    private boolean readBoolean(Reader input, char firstLetter) throws IOException {
        if (firstLetter == 't') {
            var buf = new char[3];
            input.read(buf);
            if (new String(buf).equals("rue")) {
                c = (char) input.read();
                return true;
            }
        }
        if (firstLetter == 'f') {
            var buf = new char[4];
            input.read(buf);
            if (new String(buf).equals("alse")) {
                c = (char) input.read();
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

            if (c == ',') continue;
        }
        c = (char) input.read();
        return list;
//        throw new IllegalArgumentException("Unexpected input");
    }

    private Map<String, Object> readObject(Reader input) throws IOException {
        var map = new LinkedHashMap<String, Object>();

        while (c == ',' || map.isEmpty()) {
            map.put(readString(input), parse(input));
        }

        c = (char) input.read();
        return map;
//        return emptyMap();
    }
}
