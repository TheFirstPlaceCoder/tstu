import by.thefirstplacecoder.mathtool.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationTest {

	@Test
	void shouldCreateValidEquation() {
		Equation equation = Equation.of(1, -3, 2);

		assertEquals(1, equation.a());
		assertEquals(-3, equation.b());
		assertEquals(2, equation.c());
	}

	@Test
	void shouldRecognizeLinearEquation() {
		Equation equation = Equation.of(0, 2, 3);

		assertTrue(equation.isLinear());
	}

	@Test
	void shouldRecognizeQuadraticEquation() {
		Equation equation = Equation.of(1, 2, 3);

		assertFalse(equation.isLinear());
	}

	@Test
	void shouldRejectEquationWithoutUnknownVariable() {
		MathToolException exception = assertThrows(
				MathToolException.class,
				() -> Equation.of(0, 0, 5)
		);

		assertEquals(
				"это не уравнение, неизвестное отсутствует",
				exception.getMessage()
		);
	}

	@Test
	void shouldRejectCoefficientGreaterThanMaximum() {
		assertThrows(
				MathToolException.class,
				() -> Equation.of(100_000_001, 1, 1)
		);
	}

	@Test
	void shouldRejectCoefficientLessThanMinimum() {
		assertThrows(
				MathToolException.class,
				() -> Equation.of(-100_000_001, 1, 1)
		);
	}

	@Test
	void shouldAcceptMaximumAllowedCoefficient() {
		assertDoesNotThrow(
				() -> Equation.of(100_000_000, -100_000_000, 100_000_000)
		);
	}

	@Test
	void shouldParseValidCoefficient() {
		int coefficient =
				Equation.parseCoefficient("-123", "A");

		assertEquals(-123, coefficient);
	}

	@Test
	void shouldRejectNonIntegerCoefficient() {
		MathToolException exception = assertThrows(
				MathToolException.class,
				() -> Equation.parseCoefficient("12.5", "A")
		);

		assertTrue(
				exception.getMessage()
				         .contains("не является целым числом")
		);
	}

	@Test
	void shouldRejectTextInsteadOfCoefficient() {
		assertThrows(
				MathToolException.class,
				() -> Equation.parseCoefficient("hello", "B")
		);
	}

	@Test
	void shouldRejectBlankCoefficient() {
		assertThrows(
				MathToolException.class,
				() -> Equation.parseCoefficient("   ", "C")
		);
	}
}