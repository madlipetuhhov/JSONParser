package org.example;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            var c = (char) input.read();
            if (c == '"') break;
            string.append(c);
        }

        return string.toString();
    }

    // todo: miinus nr
    private Number readNumber(Reader input, char firstNumber) throws IOException {
        var string = new StringBuilder();
        string.append(firstNumber);

        while (true) {
            var c = (char) input.read();
            if (!Character.isDigit(c) && c != '.') break;
            string.append(c);
        }
        if (string.toString().contains(".")) {
            return Double.parseDouble(string.toString());
        } else return Integer.parseInt(string.toString());
    }

    private Object readNull(Reader input) throws IOException {
//        todo: raakida mitme char korraga kontrollist
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

    private List<Object> readArray(Reader input) throws IOException {
        var list = new ArrayList<>();

//        todo: siit edasi, ei toota
//        todo: vaja kontrollida -1 koikide while(true) puhul
//       todo: vorrelda '\uFFFF' == -1 (char puhul)
        while (true) {
            var c = (char) input.read(); //
            if (c == ',' || c == '"') continue;
            if (c == ']') break;
            list.add(c);
        }

        return list;
//        return emptyList();
    }

    // objektiga viimasena tegeleda
    private Map<String, Object> readObject(Reader input) {
        return emptyMap();
    }
}
