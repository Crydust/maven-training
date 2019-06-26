package com.mycompany.example11.core;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class OperationTest {

    @Test
    public void evaluate() throws Exception {
        double result = Operation.ADD.evaluate(1.0, 1.0);
        assertThat(result, is(closeTo(2.0, 0.001)));
    }

}
