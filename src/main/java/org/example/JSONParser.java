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
    private char c;

    public Object parse(String input) {
        try {
            return parse(new StringReader(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(Reader input) throws IOException {
        while ((c = (char) input.read()) != '\uFFFF') {
            if (Character.isWhitespace(c)) continue;
            //else if (c == ':' || c == ',') return parse(input);
            else if (c == '{') return readObject(input);
            else if (c == '[') return readArray(input);
//            else if (c == ']') ;
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
        while ((c = (char) input.read()) != '\uFFFF') {
            if (c == '"' && string.isEmpty() || c == '\n' || Character.isWhitespace(c)) continue;
            else if (c == '"'){
                c = (char)input.read();
                break;
            }
            string.append(c);
        }
        return string.toString();
    }

    private Number readNumber(Reader input, char firstNumber) throws IOException {
        var string = new StringBuilder();
        string.append(firstNumber);
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

    // kuidas tegeleda valede exceptionitega

    //todo: teha rekursiivseks
    private List<Object> readArray(Reader input) throws IOException {
        var list = new ArrayList<>();

        while (c != ']') {
            list.add(parse(input));
            if(c == ','){
                continue;
            }
        }
        return list;
    }
//        if (list.isEmpty()) {
//            return emptyList();
//        }
//        return list;
//        throw new IllegalArgumentException("Unexpected input");

    private Map<String, Object> readObject(Reader input) throws IOException {
        var map = new LinkedHashMap<String, Object>();
        while(c == '\uFFFF'){
            map.put(readString(input), parse(input));
        }
        return map;
//        return emptyMap();
    }
}
