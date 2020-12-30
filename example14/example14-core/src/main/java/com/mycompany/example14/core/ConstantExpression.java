package com.mycompany.example14.core;

import com.mycompany.example14.api.Expression;

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
