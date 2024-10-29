package org.example;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Lexer {

    public Map<String, TokenType> tokenize(String input) {
        var stringReader = new StringReader(input);
//        try {
////            reader(stringReader);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        String[] splitInput = input.replaceAll("\\s+", "").trim().split("(?<=[{}:])|(?=\\{|}|:|\\s+)");
        var tokens = new LinkedHashMap<String, TokenType>();

        for (String s : splitInput) {
            TokenType tokenType = getTokenType(s);
            tokens.put(s, tokenType);
        }
        return tokens;
    }

//    private static void reader(Reader input) throws IOException {
//        int charReader = input.read();
//        if (input.read() == '{')
//
//    }

    private TokenType getTokenType(String s) {
        return switch (s) {
            case "{" -> TokenType.CURLY_BRACE_OPEN;
            case "}" -> TokenType.CURLY_BRACE_CLOSE;
            case "[" -> TokenType.SQUARE_BRACE_OPEN;
            case "]" -> TokenType.SQUARE_BRACE_CLOSE;
            case ":" -> TokenType.COLON;
            case "," -> TokenType.COMMA;
            case "true", "false" -> TokenType.BOOLEAN;
            default -> {
                if (s.startsWith("\"") && s.endsWith("\"")) {
                    yield TokenType.STRING;
                }
//                if (Integer.parseInt(s) Double.parseDouble(s)){
//                    yield TokenType.NUMBER;
//                }
                yield TokenType.INVALID;
            }
        };
    }


    public static void main(String[] args) {
        String inputString = "Hello, World!";

        try (StringReader stringReader = new StringReader(inputString)) {
            int charRead;
            while ((charRead = stringReader.read()) != -1) {
                System.out.print((char) charRead); // Print each character
            }
            System.out.println(); // New line after reading
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
