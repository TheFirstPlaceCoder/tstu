package by.thefirstplacecoder.mathtool.equation;

import by.thefirstplacecoder.mathtool.impl.equation.Equation;
import by.thefirstplacecoder.mathtool.impl.equation.EquationSolution;
import by.thefirstplacecoder.mathtool.impl.equation.EquationSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationSolverTest {

    private final EquationSolver solver = new EquationSolver();

    @Test
    void shouldSolveQuadraticEquation() {
        EquationSolution solution = solver.solve(Equation.of(1, -5, 6));
        var result = (EquationSolution.TwoRootsSolution) solution;

        assertEquals(3.0, result.firstRoot(), 0.0001);
        assertEquals(2.0, result.secondRoot(), 0.0001);
    }

    @Test
    void shouldSolveLinearEquation() {
        EquationSolution solution = solver.solve(Equation.of(0, 2, -4));
        var result = (EquationSolution.LinearSolution) solution;

        assertEquals(2.0, result.root(), 0.0001);
    }
}
