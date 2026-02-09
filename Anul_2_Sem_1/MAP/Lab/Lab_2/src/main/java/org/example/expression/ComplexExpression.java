package org.example.expression;

import org.example.domain.ComplexNumber;
import org.example.domain.Operation;

public abstract class ComplexExpression {
    protected final ComplexNumber op1;
    protected final ComplexNumber op2;
    protected final Operation op;

    protected ComplexExpression(ComplexNumber op1, ComplexNumber op2, Operation op) {
        this.op1 = op1;
        this.op2 = op2;
        this.op = op;
    }

    public abstract ComplexNumber execute();
}
