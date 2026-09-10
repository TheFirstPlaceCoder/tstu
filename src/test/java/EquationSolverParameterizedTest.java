import by.thefirstplacecoder.mathtool.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class EquationSolverParameterizedTest {

	private final EquationSolver solver =
			new EquationSolver();

	@ParameterizedTest(
			name = "{0}x² + {1}x + {2} = 0 -> x1={3}, x2={4}"
	)
	@CsvSource({
			"1, -5, 6, 3.0, 2.0",
			"1, -3, 2, 2.0, 1.0",
			"1,  3, 2, -1.0, -2.0",
			"2, -6, 4, 2.0, 1.0"
	})
	void shouldSolveEquationsWithTwoRoots(
			int a,
			int b,
			int c,
			double expectedX1,
			double expectedX2
	) {
		Equation equation = Equation.of(a, b, c);

		EquationSolution solution =
				solver.solve(equation);

		assertInstanceOf(
				EquationSolution.TwoRootsSolution.class,
				solution
		);

		EquationSolution.TwoRootsSolution result =
				(EquationSolution.TwoRootsSolution) solution;

		assertEquals(
				expectedX1,
				result.firstRoot(),
				1e-9
		);

		assertEquals(
				expectedX2,
				result.secondRoot(),
				1e-9
		);
	}
}