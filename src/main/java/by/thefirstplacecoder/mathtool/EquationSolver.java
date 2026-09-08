package by.thefirstplacecoder.mathtool;

public final class EquationSolver {

	public EquationSolution solve(Equation equation) {
		if (equation == null) {
			throw new IllegalArgumentException(
					"выражение не должно быть null"
			);
		}

		if (equation.isLinear()) {
			return solveLinear(equation);
		}

		return solveQuadratic(equation);
	}

	private EquationSolution solveLinear(Equation equation) {
		double root = -(double) equation.c() / equation.b();

		return new EquationSolution.LinearSolution(root);
	}

	private EquationSolution solveQuadratic(Equation equation) {
		long discriminant = calculateDiscriminant(equation);

		if (discriminant > 0) {
			double sqrtDiscriminant = Math.sqrt(discriminant);
			double denominator = 2.0 * equation.a();

			double firstRoot =
					(-equation.b() + sqrtDiscriminant)
							/ denominator;

			double secondRoot =
					(-equation.b() - sqrtDiscriminant)
							/ denominator;

			return new EquationSolution.TwoRootsSolution(
					discriminant,
					firstRoot,
					secondRoot
			);
		}

		if (discriminant == 0) {
			double root =
					-(double) equation.b()
							/ (2.0 * equation.a());

			return new EquationSolution.SingleRootSolution(
					discriminant,
					root
			);
		}

		return new EquationSolution.NoRealRootsSolution(
				discriminant
		);
	}

	private long calculateDiscriminant(Equation equation) {
		long a = equation.a();
		long b = equation.b();
		long c = equation.c();

		return b * b - 4L * a * c;
	}
}