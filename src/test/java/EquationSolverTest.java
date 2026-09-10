import by.thefirstplacecoder.mathtool.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationSolverTest {

	private final EquationSolver solver = new EquationSolver();

	@Test
	void shouldSolveQuadraticEquationWithTwoRoots() {
		Equation equation = Equation.of(1, -5, 6);

		EquationSolution solution = solver.solve(equation);

		assertInstanceOf(
				EquationSolution.TwoRootsSolution.class,
				solution
		);

		EquationSolution.TwoRootsSolution result =
				(EquationSolution.TwoRootsSolution) solution;

		assertEquals(1, result.discriminant());
		assertEquals(3.0, result.firstRoot(), 1e-9);
		assertEquals(2.0, result.secondRoot(), 1e-9);
	}

	@Test
	void shouldSolveQuadraticEquationWithSingleRoot() {
		Equation equation = Equation.of(1, -2, 1);

		EquationSolution solution = solver.solve(equation);

		assertInstanceOf(
				EquationSolution.SingleRootSolution.class,
				solution
		);

		EquationSolution.SingleRootSolution result =
				(EquationSolution.SingleRootSolution) solution;

		assertEquals(0, result.discriminant());
		assertEquals(1.0, result.root(), 1e-9);
	}

	@Test
	void shouldReturnNoRealRootsWhenDiscriminantIsNegative() {
		Equation equation = Equation.of(1, 0, 1);

		EquationSolution solution = solver.solve(equation);

		assertInstanceOf(
				EquationSolution.NoRealRootsSolution.class,
				solution
		);

		EquationSolution.NoRealRootsSolution result =
				(EquationSolution.NoRealRootsSolution) solution;

		assertEquals(-4, result.discriminant());
	}

	@Test
	void shouldSolveLinearEquationWhenAIsZero() {
		Equation equation = Equation.of(0, 2, -4);

		EquationSolution solution = solver.solve(equation);

		assertInstanceOf(
				EquationSolution.LinearSolution.class,
				solution
		);

		EquationSolution.LinearSolution result =
				(EquationSolution.LinearSolution) solution;

		assertEquals(2.0, result.root(), 1e-9);
	}

	@Test
	void shouldCorrectlySolveEquationWithNegativeA() {
		Equation equation = Equation.of(-1, 5, -6);

		EquationSolution solution = solver.solve(equation);

		assertInstanceOf(
				EquationSolution.TwoRootsSolution.class,
				solution
		);

		EquationSolution.TwoRootsSolution result =
				(EquationSolution.TwoRootsSolution) solution;

		assertEquals(1, result.discriminant());

		assertEquals(2.0, result.firstRoot(), 1e-9);
		assertEquals(3.0, result.secondRoot(), 1e-9);
	}

	@Test
	void shouldRejectNullEquation() {
		assertThrows(
				IllegalArgumentException.class,
				() -> solver.solve(null)
		);
	}
}