package com.mycompany.example10.api;

public interface ExpressionParser {

    Expression parse(String s) throws ParsingFailedException;

}
