package com.mycompany.example11.api;

public interface ExpressionParser {

    Expression parse(String s) throws ParsingFailedException;

}
