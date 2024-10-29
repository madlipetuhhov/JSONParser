package org.example;

import java.util.ArrayList;
import java.util.List;

public class Token {
    private final TokenType type;
    private final List<Token> tokens = new ArrayList<>();

    public Token(TokenType type) {
        this.type = type;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public TokenType getType() {
        return type;
    }
}
