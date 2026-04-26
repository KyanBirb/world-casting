package dev.kyanbirb.world_casting.content.arithmetic;

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException;
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator;
import at.petrak.hexcasting.api.casting.math.HexPattern;

import java.util.List;

public class QuaternionArithmetic implements Arithmetic {
    private static final List<HexPattern> OPS = List.of(
            MUL
    );

    @Override
    public String arithName() {
        return "quaternion_maths";
    }

    @Override
    public Iterable<HexPattern> opTypes() {
        return OPS;
    }

    @Override
    public Operator getOperator(HexPattern hexPattern) {
        if(hexPattern == MUL) {
            return new OperatorQuaternionMul();
        }
        throw new InvalidOperatorException(hexPattern + " is not a valid operator in Arithmetic " + this);
    }
}
