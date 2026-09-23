package by.thefirstplacecoder.mathtool.impl.equation;

import java.io.PrintStream;
import java.util.Locale;

public class SolutionPrinter {

	public void print(
			EquationSolution solution,
			PrintStream out
	) {
		switch (solution) {
			case EquationSolution.LinearSolution linear ->
					printLinear(linear, out);

			case EquationSolution.TwoRootsSolution twoRoots ->
					printTwoRoots(twoRoots, out);

			case EquationSolution.SingleRootSolution singleRoot ->
					printSingleRoot(singleRoot, out);

			case EquationSolution.NoRealRootsSolution noRoots ->
					printNoRoots(noRoots, out);
		}
	}

	private void printLinear(
			EquationSolution.LinearSolution solution,
			PrintStream out
	) {
		out.println("Линейное уравнение");
		printNumber(out, "x", solution.root());
	}

	private void printTwoRoots(
			EquationSolution.TwoRootsSolution solution,
			PrintStream out
	) {
		out.println("Уравнение квадратное");

		printDiscriminant(
				out,
				solution.discriminant()
		);

		printNumber(
				out,
				"x1",
				solution.firstRoot()
		);

		printNumber(
				out,
				"x2",
				solution.secondRoot()
		);
	}

	private void printSingleRoot(
			EquationSolution.SingleRootSolution solution,
			PrintStream out
	) {
		out.println("Уравнение квадратное");

		printDiscriminant(
				out,
				solution.discriminant()
		);

		printNumber(
				out,
				"x",
				solution.root()
		);
	}

	private void printNoRoots(
			EquationSolution.NoRealRootsSolution solution,
			PrintStream out
	) {
		out.println("Уравнение квадратное");

		printDiscriminant(
				out,
				solution.discriminant()
		);

		out.println("Действительных корней нет");
	}

	private void printDiscriminant(
			PrintStream out,
			long discriminant
	) {
		out.printf(
				Locale.ROOT,
				"Дискриминант: %.3f%n",
				(double) discriminant
		);
	}

	private void printNumber(
			PrintStream out,
			String name,
			double value
	) {
		double normalized = normalizeZero(value);

		out.printf(
				Locale.ROOT,
				"%s = %.3f%n",
				name,
				normalized
		);
	}

	private double normalizeZero(double value) {
		return value == 0.0 ? 0.0 : value;
	}
}
