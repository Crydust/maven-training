package com.mycompany.example8.calculator;

import org.json.JSONObject;

public final class ExpressionParser {

    public Expression parse(String s) {
        return parseOperationExpression(new JSONObject(s));
    }

    private Expression parseOperationExpression(JSONObject o) {
        final Expression a = parseExpression(o, "a");
        final Operation operation = o.getEnum(Operation.class, "operation");
        final Expression b = parseExpression(o, "b");
        return new OperationExpression(a, operation, b);
    }

    private Expression parseExpression(JSONObject o, String key) {
        final Object expr = o.get(key);
        if (expr instanceof Number) {
            return new ConstantExpression(((Number) expr).doubleValue());
        }
        if (expr instanceof JSONObject) {
            return parseOperationExpression((JSONObject) expr);
        }
        throw new IllegalArgumentException("I can't parse this.");
    }
}
