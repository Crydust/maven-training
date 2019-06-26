package com.mycompany.example7;

import com.mycompany.example7.calculator.ExpressionParser;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("args.length = " + args.length);
        if (args.length != 1) {
            System.out.println("An argument was expected, but none was given.");
            return;
        }
        String input = args[0];
        System.out.println("input = " + input);
        double output = new ExpressionParser().parse(input).getResult();
        System.out.println("output = " + output);
    }
}
