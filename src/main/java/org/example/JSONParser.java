package org.example;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static org.example.TokenType.*;

public class JSONParser {
    Lexer lexer = new Lexer();

    public Object parse(String input) {
        var lexerMap = lexer.tokenize(input);

        for (Map.Entry<String, TokenType> entry : lexerMap.entrySet()){
            if (entry.getValue().equals(STRING)) return entry.getKey();

        }
//        return Integer.parseInt(input);
       return emptyMap();
    }

}
