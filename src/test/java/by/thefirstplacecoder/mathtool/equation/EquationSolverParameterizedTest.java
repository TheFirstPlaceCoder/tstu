package by.thefirstplacecoder.mathtool.equation;

import by.thefirstplacecoder.mathtool.impl.equation.Equation;
import by.thefirstplacecoder.mathtool.impl.equation.EquationSolution;
import by.thefirstplacecoder.mathtool.impl.equation.EquationSolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class EquationSolverParameterizedTest {

    private final EquationSolver solver = new EquationSolver();

    @ParameterizedTest
    @CsvSource({
            "1, -5, 6, 3.0, 2.0",
            "1, -3, 2, 2.0, 1.0",
            "1, 3, 2, -1.0, -2.0"
    })
    void shouldSolveDifferentEquations(int a, int b, int c, double x1, double x2) {
        var result = (EquationSolution.TwoRootsSolution) solver.solve(Equation.of(a, b, c));

        assertEquals(x1, result.firstRoot(), 0.0001);
        assertEquals(x2, result.secondRoot(), 0.0001);
    }
}
