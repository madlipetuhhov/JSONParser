package org.example;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    Lexer lexer = new Lexer();

    @Test
    void something() {
        var listFromString = lexer.createListFromString("    {\"key\": \"value\"}  ");
        var tokenList = lexer.tokenize(listFromString);
        // TODO assert
        System.out.println(tokenList);
    }
}