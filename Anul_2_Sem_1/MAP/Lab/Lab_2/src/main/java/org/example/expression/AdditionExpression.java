package org.example.expression;

import org.example.domain.ComplexNumber;
import org.example.domain.Operation;

public class AdditionExpression extends ComplexExpression {
    protected AdditionExpression(ComplexNumber op1, ComplexNumber op2, Operation op) {
        super(op1, op2, op);
    }

    @Override
    public ComplexNumber execute() {
        return op1.add(op2);
    }
}
