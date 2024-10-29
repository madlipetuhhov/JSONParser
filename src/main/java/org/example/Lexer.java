package org.example;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Lexer {
    String[] createListFromString(String input) {
        return input.replaceAll("\\s+", "").trim().split("(?<=[{}:])|(?=\\{|}|:|\\s+)");
    }

    public Map<String, TokenType> tokenize(String[] stringParts) {
        var tokens = new LinkedHashMap<String, TokenType>();

        for (String s : stringParts) {
            TokenType tokenType = getTokenType(s);
            tokens.put(s, tokenType);
        }
        return tokens;
    }

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
                yield TokenType.INVALID;
            }
        };
    }
}
