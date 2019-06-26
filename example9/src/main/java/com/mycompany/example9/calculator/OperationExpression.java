package com.mycompany.example9.calculator;

class OperationExpression implements Expression {

    private final Expression a;
    private final Operation operation;
    private final Expression b;

    OperationExpression(Expression a, Operation operation, Expression b) {
        this.a = a;
        this.operation = operation;
        this.b = b;
    }

    public double getResult() {
        return operation.evaluate(a.getResult(), b.getResult());
    }
}
