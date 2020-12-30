package com.mycompany.example14.core;

import com.mycompany.example14.api.Expression;

public class OperationExpression implements Expression {

    private final Expression a;
    private final Operation operation;
    private final Expression b;

    public OperationExpression(Expression a, Operation operation, Expression b) {
        this.a = a;
        this.operation = operation;
        this.b = b;
    }

    public double getResult() {
        return operation.evaluate(a.getResult(), b.getResult());
    }
}
