package by.thefirstplacecoder.mathtool.equation;

import by.thefirstplacecoder.mathtool.common.MathToolException;
import by.thefirstplacecoder.mathtool.impl.equation.Equation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationTest {

    @Test
    void shouldAcceptMaximumValue() {
        assertDoesNotThrow(() -> Equation.of(10_000, -10_000, 10_000));
    }

    @Test
    void shouldRejectTooLargeValue() {
        assertThrows(MathToolException.class, () -> Equation.of(10_001, 1, 1));
    }

    @Test
    void shouldRejectEquationWithoutX() {
        assertThrows(MathToolException.class, () -> Equation.of(0, 0, 5));
    }
}
