package com.mycompany.example14.api;

public interface ExpressionParser {

    Expression parse(String s) throws ParsingFailedException;

}
