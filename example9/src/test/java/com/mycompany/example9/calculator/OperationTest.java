package com.mycompany.example9.calculator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class OperationTest {

    @Test
    public void evaluate() throws Exception {
        double result = Operation.ADD.evaluate(1.0, 1.0);
        assertThat(result, is(closeTo(2.0, 0.001)));
    }

}
