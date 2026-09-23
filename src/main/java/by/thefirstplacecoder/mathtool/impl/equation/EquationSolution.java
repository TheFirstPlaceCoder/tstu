package by.thefirstplacecoder.mathtool.impl.equation;

public sealed interface EquationSolution
		permits EquationSolution.LinearSolution,
		EquationSolution.TwoRootsSolution,
		EquationSolution.SingleRootSolution,
		EquationSolution.NoRealRootsSolution {

	record LinearSolution(
			double root
	) implements EquationSolution {
	}

	record TwoRootsSolution(
			long discriminant,
			double firstRoot,
			double secondRoot
	) implements EquationSolution {
	}

	record SingleRootSolution(
			long discriminant,
			double root
	) implements EquationSolution {
	}

	record NoRealRootsSolution(
			long discriminant
	) implements EquationSolution {
	}
}
