package com.mycompany.example9.calculator;

class ConstantExpression implements Expression {
    private final double value;

    ConstantExpression(double value) {
        this.value = value;
    }

    @Override
    public double getResult() {
        return value;
    }
}
