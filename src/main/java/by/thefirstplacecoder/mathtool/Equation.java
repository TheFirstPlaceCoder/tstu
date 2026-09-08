package by.thefirstplacecoder.mathtool;

public record Equation(
		int a,
		int b,
		int c
) {

	public static final int MAX_COEFFICIENT_ABS_VALUE = 100_000_000;

	public Equation {
		validateRange(a, "A");
		validateRange(b, "B");
		validateRange(c, "C");

		if (a == 0 && b == 0) {
			throw new MathToolException(
					"это не уравнение, неизвестное отсутствует"
			);
		}
	}

	public static Equation of(int a, int b, int c) {
		return new Equation(a, b, c);
	}

	public static int parseCoefficient(String value, String name) {
		if (value == null || value.isBlank()) {
			throw new MathToolException(
					"коэффициент " + name + " не задан"
			);
		}

		final int coefficient;

		try {
			coefficient = Integer.parseInt(value.trim());
		} catch (NumberFormatException exception) {
			throw new MathToolException(
					"коэффициент " + name
							+ " не является целым числом"
			);
		}

		validateRange(coefficient, name);

		return coefficient;
	}

	private static void validateRange(int value, String name) {
		if (Math.abs((long) value) > MAX_COEFFICIENT_ABS_VALUE) {
			throw new MathToolException(
					"коэффициент " + name
							+ " находится вне допустимого диапазона "
							+ "[-"
							+ MAX_COEFFICIENT_ABS_VALUE
							+ "; "
							+ MAX_COEFFICIENT_ABS_VALUE
							+ "]"
			);
		}
	}

	public boolean isLinear() {
		return a == 0;
	}
}