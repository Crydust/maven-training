package com.mycompany.example9.calculator;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ExpressionParserIT {

    @Test
    public void parseSimple() throws Exception {
        final double result = new ExpressionParser()
                .parse("{\"a\":1.0, \"operation\":\"ADD\", \"b\":1.0}")
                .getResult();
        assertThat(result, is(closeTo(2.0, 0.001)));
    }

    @Test
    public void parseNested() throws Exception {
        final double result = new ExpressionParser()
                .parse("{\"a\":1.0, \"operation\":\"ADD\", \"b\":{\"a\":1.0, \"operation\":\"ADD\", \"b\":1.0}}")
                .getResult();
        assertThat(result, is(closeTo(3.0, 0.001)));
    }
}
