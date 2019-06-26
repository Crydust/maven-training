package com.mycompany.example10.core;

public enum Operation {
    EXPONENT {
        @Override
        double evaluate(double a, double b) {
            return Math.pow(a, b);
        }
    }, DIVIDE {
        @Override
        double evaluate(double a, double b) {
            return a / b;
        }
    }, MULTIPLY {
        @Override
        double evaluate(double a, double b) {
            return a * b;
        }
    }, ADD {
        @Override
        double evaluate(double a, double b) {
            return a + b;
        }
    }, SUBTRACT {
        @Override
        double evaluate(double a, double b) {
            return a - b;
        }
    };

    abstract double evaluate(double a, double b);
}
