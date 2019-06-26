package com.mycompany.example10.core;

import com.mycompany.example10.api.Expression;

public class ConstantExpression implements Expression {
    private final double value;

    public ConstantExpression(double value) {
        this.value = value;
    }

    @Override
    public double getResult() {
        return value;
    }
}
