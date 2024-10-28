package org.example;

import java.util.HashMap;

public class Lexer {
    // 1. tykkideks
    // 2. maara tokenType
    // 3.

    public static String[] createListFromString(String input) {
        String[] parts = input.split("(?<=[{}:])|(?=\\{|}|:|\\s+)");
        return parts;
    }

    public static HashMap<String, TokenType> tokenize(String[] stringParts) {
        HashMap<String, TokenType> tokenList = new HashMap<>();

        for (String s : stringParts) {
            TokenType tokenType = getTokenType(s);
            tokenList.put(s, tokenType);
        }
        return tokenList;
    }

    private static TokenType getTokenType(String s) {
        switch (s) {
            case "{":
                return TokenType.BRACE_OPEN;
            case "}":
                return TokenType.BRACE_CLOSE;
            case ":":
                return TokenType.COLON;
            case ",":
                return TokenType.COMMA;
            case "true":
            case "false":
                return TokenType.BOOLEAN;
            default:
                if (s.startsWith("\"") && s.endsWith("\"")) {
                    return TokenType.STRING;
                } else if (Integer.parseInt(s)){
                    return TokenType.NUMBER;
                }
        }

        public boolean isValid (String input){

            String[] stringParts = createListFromString(input);

//        for (int i = 0; i < stringParts.length; i++){
//            if( i == 0 && stringParts[0]. == TokenType.LEFT_BRACE){
//
//            }
//        }
            return true;
        }

        public static void main (String[]args){
            String[] parts = createListFromString("{\"key\":\"value\"}");
            for (String s : parts) {
                System.out.println("[" + s + "]");
            }
        }
    }
