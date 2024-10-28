package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Token {
    public final TokenType type;
    public List<Token> tokens = new ArrayList<>();

    public Token(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }
}
