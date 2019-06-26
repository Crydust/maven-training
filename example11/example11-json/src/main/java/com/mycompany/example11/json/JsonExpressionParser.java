package com.mycompany.example11.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.mycompany.example11.api.Expression;
import com.mycompany.example11.api.ExpressionParser;
import com.mycompany.example11.api.ParsingFailedException;
import com.mycompany.example11.core.ConstantExpression;
import com.mycompany.example11.core.Operation;
import com.mycompany.example11.core.OperationExpression;

public final class JsonExpressionParser implements ExpressionParser {

    @Override
    public Expression parse(String s) throws ParsingFailedException {
        try {
            return parseOperationExpression(new JSONObject(s));
        } catch (JSONException ex) {
            throw new ParsingFailedException(ex);
        }
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
