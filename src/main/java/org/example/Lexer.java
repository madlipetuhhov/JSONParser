package org.example;

import java.util.HashMap;
import java.util.List;

public class Lexer {
    // 1. tykkideks
    // 2. maara type
    // 3.

    public static String[] createListFromString(String input) {
        String[] parts = input.split("(?<=[{}:])|(?=\\{|}|:|\\s+)");
        return parts;
    }

    public static HashMap<String,TokenType> tokenize (String [] stringParts){
        HashMap<String, TokenType> tokenList = new HashMap<>();

        for(String s : stringParts){
            tokenList.put(s, s.)
        }
    }

    public boolean isValid (String input){

        String [] stringParts = createListFromString(input);

//        for (int i = 0; i < stringParts.length; i++){
//            if( i == 0 && stringParts[0]. == TokenType.LEFT_BRACE){
//
//            }
//        }
        return true;
    }

    public static void main(String[] args) {
        String[] parts = createListFromString("{\"key\":\"value\"}");
        for (String s : parts) {
            System.out.println("[" + s + "]");
        }
    }
}
