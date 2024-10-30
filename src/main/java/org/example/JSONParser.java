package org.example;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

public class JSONParser {
    public Object parse(String input) {
        try {
            return parse(new StringReader(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(Reader input) throws IOException {
        var read = input.read();
        var c = (char) read;
        while (read != -1) {
            if (Character.isWhitespace(c)) return parse(input);
            else if (c == ':') return parse(input);
            else if (c == '{') return readObject(input);
            else if (c == '[') return readArray(input);
            else if (c == '"') return readString(input);
            else if (Character.isDigit(c) || c == '-') return readNumber(input, c);
            else if (c == 'n') return readNull(input);
            else if (Character.isAlphabetic(c)) return readBoolean(input, c);
            else throw new IllegalArgumentException("Unexpected character " + c);
        }
        throw new IllegalArgumentException("Unexpected end");
    }

    private String readString(Reader input) throws IOException {
        var string = new StringBuilder();
        char c;
//        topelt jutumark
        while ((c = (char) input.read()) != '\uFFFF') {
            if (c == '"' && string.isEmpty()) continue;
            else if (c == '"') break;
            string.append(c);
        }
        return string.toString();
    }

    private Number readNumber(Reader input, char firstNumber) throws IOException {
        var string = new StringBuilder();
        string.append(firstNumber);
        char c;
        while ((c = (char) input.read()) != '\uFFFF') {

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
            return null;
        }
        throw new IllegalArgumentException("Unexpected input");
    }

    private boolean readBoolean(Reader input, char firstLetter) throws IOException {
        if (firstLetter == 't') {
            var buf = new char[3];
            input.read(buf);
            if (new String(buf).equals("rue")) {
                return true;
            }
        }
        if (firstLetter == 'f') {
            var buf = new char[4];
            input.read(buf);
            if (new String(buf).equals("alse")) {
                return false;
            }
        }
        throw new IllegalArgumentException("Unexpected input");
    }

    //    todo: kas booleanid aitavad if lausete sees, kuidas ilusamaks teha
    // kuidas tegeleda valede exceptionitega
    private List<Object> readArray(Reader input) throws IOException {
        var list = new ArrayList<>();
        var string = new StringBuilder();
        int countQuotes = 0;
//        boolean isNumber = countQuotes == 0;
        char c;

        while ((c = (char) input.read()) != '\uFFFF') {
            if (Character.isWhitespace(c)) continue;
            if (c == ',' && countQuotes > 0) continue;
//            boolean isString = c == '"';

            if (c == '"') {
                countQuotes++;
                if (countQuotes % 2 == 0) {
                    list.add(string.toString());
                    string = new StringBuilder();
                }
                continue;
            }

            if (countQuotes == 0) {
                if (c == ',' || (c == ']' && !list.isEmpty())) {
                    if (string.toString().contains(".")) {
                        list.add(Double.parseDouble(string.toString()));
                    } else {
                        list.add(Integer.parseInt(string.toString()));
                    }
                    string = new StringBuilder();
                    continue;
                }
            }
            if (c == ']') break;
            string.append(c);
        }
        if (list.isEmpty()) {
            return emptyList();
        }
        return list;
//        throw new IllegalArgumentException("Unexpected input");
    }

    // todo: objektiga viimasena tegeleda
    private Map<String, Object> readObject(Reader input) throws IOException {
        var map = new LinkedHashMap<String, Object>();
        map.put(readString(input), parse(input));
        return map;
//        return emptyMap();
    }
}
