import by.thefirstplacecoder.mathtool.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MathToolApplicationTest {

	@Test
	void shouldSolveEquationPassedThroughCommandLine() {
		ByteArrayOutputStream output =
				new ByteArrayOutputStream();

		PrintStream printStream =
				new PrintStream(output);

		MathToolApplication application =
				new MathToolApplication(
						new CommandLineParser(),
						new EquationSolver(),
						new SolutionPrinter(),
						new Scanner(""),
						printStream
				);

		String[] args = {
				"solve",
				"-a", "1",
				"-b", "-3",
				"-c", "2"
		};

		int exitCode = application.run(args);

		String result = output.toString();

		assertEquals(0, exitCode);

		assertTrue(
				result.contains("Уравнение квадратное")
		);

		assertTrue(
				result.contains("x1 = 2.000")
		);

		assertTrue(
				result.contains("x2 = 1.000")
		);
	}

	@Test
	void shouldHandleInteractiveInput() {
		ByteArrayOutputStream output =
				new ByteArrayOutputStream();

		PrintStream printStream =
				new PrintStream(output);

		Scanner scanner =
				new Scanner("""
                        1
                        -3
                        2
                        """);

		MathToolApplication application =
				new MathToolApplication(
						new CommandLineParser(),
						new EquationSolver(),
						new SolutionPrinter(),
						scanner,
						printStream
				);

		int exitCode = application.run(
				new String[]{"solve"}
		);

		String result = output.toString();

		assertEquals(0, exitCode);

		assertTrue(
				result.contains("x1 = 2.000")
		);

		assertTrue(
				result.contains("x2 = 1.000")
		);
	}

	@Test
	void shouldReturnErrorExitCodeForInvalidInput() {
		ByteArrayOutputStream output =
				new ByteArrayOutputStream();

		MathToolApplication application =
				new MathToolApplication(
						new CommandLineParser(),
						new EquationSolver(),
						new SolutionPrinter(),
						new Scanner(""),
						new PrintStream(output)
				);

		String[] args = {
				"solve",
				"-a", "hello",
				"-b", "2",
				"-c", "3"
		};

		int exitCode = application.run(args);

		assertEquals(1, exitCode);

		assertTrue(
				output.toString()
				      .contains("ОШИБКА")
		);
	}
}