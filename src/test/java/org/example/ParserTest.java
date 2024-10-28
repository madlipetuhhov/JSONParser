package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    Parser parser = new Parser();
    @Test
    void emptyObject() {
       assertEquals("{}", parser.getEmptyObject());
    }
}