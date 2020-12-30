package com.mycompany.example14.cli;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.mycompany.example14.api.ExpressionParser;
import com.mycompany.example14.api.ParsingFailedException;

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
        Iterator<ExpressionParser> iterator = loadExpressionParsers().iterator();
        while (iterator.hasNext()) {
            ExpressionParser parser = iterator.next();
            try {
                double output = parser.parse(input).getResult();
                System.out.println("output = " + output);
            } catch (ParsingFailedException e) {
                System.out.println("This parser could not understand the input.");
                if (iterator.hasNext()) {
                    System.out.println("We'll try another one.");
                } else {
                    System.out.println("Unfortunately that was the last parser we could find.");
                    e.printStackTrace();
                }
            }
        }
    }

    private static Iterable<ExpressionParser> loadExpressionParsers() {
        return ServiceLoader.load(ExpressionParser.class);
    }
}
