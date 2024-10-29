package org.example;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
            else throw new IllegalArgumentException("Unexpected character " + c);
        }
        throw new IllegalArgumentException("Unexpected end");
    }

    private String readString(Reader input) throws IOException {
        var string = new StringBuilder();
        string.append('"');

        boolean isSecondQuote = false;
        while (!isSecondQuote) {
            var read = input.read();
            var c = (char) read;

            string.append(c);
            if (c == '"') {
                isSecondQuote = true;
            }
        }

        return string.toString();
    }

    private Number readNumber(Reader input, char firstNumber) throws IOException {
        var string = new StringBuilder();
        string.append(firstNumber);

        while (true) {
            var read = input.read();
            var c = (char) read;
            if (!Character.isDigit(c)) break;
            string.append(c);
        }
        return Integer.parseInt(string.toString());
    }

    private List<Object> readArray(Reader input) {
        return emptyList();
    }

    // todo: miinus nr, boolean, null
    // objektiga viimasena tegeleda
    private Map<String, Object> readObject(Reader input) {
        return emptyMap();
    }
}
