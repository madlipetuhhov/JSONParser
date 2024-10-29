package org.example;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
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
            if (Character.isWhitespace(c)) continue;
            else if (c == '{') return readObject(input);
            else if (c == '[') return readArray(input);
            else if (c == '"') return readString(input);
            else if (Character.isDigit(c)) return readNumber(input, c);
            else if (c == 'n') return readNull(input);
            else if (Character.isAlphabetic(c)) return readBoolean(input, c);
            else throw new IllegalArgumentException("Unexpected character " + c);
        }
        throw new IllegalArgumentException("Unexpected end");
    }

    private String readString(Reader input) throws IOException {
        var string = new StringBuilder();
        while (true) {
            var c = getChar(input);
            if (c == '"') break;
            string.append(c);
        }

        return string.toString();
    }

    private Number readNumber(Reader input, char firstNumber) throws IOException {
        var string = new StringBuilder();
        string.append(firstNumber);

        while (true) {
            var c = getChar(input);
            if (!Character.isDigit(c) && c != '.') break;
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
        var string = new StringBuilder();
        string.append(firstLetter);

        while (true) {
            var c = getChar(input);
            if (!Character.isAlphabetic(c)) break;
            string.append(c);
        }

        if ("true".contentEquals(string)) return true;
        if ("false".contentEquals(string)) return false;
        throw new IllegalArgumentException("Unexpected input");
    }

    private List<Object> readArray(Reader input) throws IOException {
        var list = new ArrayList<>();

//        todo: siit edasi
//        todo vaja kontrollida -1 koikide while(true) puhul
        while (true) {
            var c = getChar(input);
            if (c == ',' || c == '"') continue;
            if (c == ']') break;
            list.add(c);
        }

        return list;
//        return emptyList();
    }

    // todo: miinus nr, boolean, null
    // objektiga viimasena tegeleda
    private Map<String, Object> readObject(Reader input) {
        return emptyMap();
    }

    private static char getChar(Reader input) throws IOException {
        return (char) input.read(); // '\uFFFF' == -1 (char puhul)
    }
}
