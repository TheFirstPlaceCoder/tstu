package by.thefirstplacecoder.mathtool;

import java.io.PrintStream;
import java.util.Scanner;

public class MathToolApplication {

	private static final int SUCCESS = 0;
	private static final int ERROR = 1;

	private final CommandLineParser commandLineParser;
	private final EquationSolver equationSolver;
	private final SolutionPrinter solutionPrinter;

	private final Scanner scanner;
	private final PrintStream out;

	public MathToolApplication() {
		this(
				new CommandLineParser(),
				new EquationSolver(),
				new SolutionPrinter(),
				new Scanner(System.in),
				System.out
		);
	}

	public MathToolApplication(
			CommandLineParser commandLineParser,
			EquationSolver equationSolver,
			SolutionPrinter solutionPrinter,
			Scanner scanner,
			PrintStream out
	) {
		this.commandLineParser = commandLineParser;
		this.equationSolver = equationSolver;
		this.solutionPrinter = solutionPrinter;
		this.scanner = scanner;
		this.out = out;
	}

	public int run(String[] args) {
		try {
			CommandLineParser.Command command = commandLineParser.parse(args);

			return switch (command) {
				case CommandLineParser.HelpCommand ignored -> {
					printHelp();
					yield SUCCESS;
				}

				case CommandLineParser.InteractiveSolveCommand ignored -> {
					Equation equation = readEquation();
					solveAndPrint(equation);
					yield SUCCESS;
				}

				case CommandLineParser.SolveCommand solveCommand -> {
					solveAndPrint(solveCommand.equation());
					yield SUCCESS;
				}
			};

		} catch (MathToolException exception) {
			out.println("ОШИБКА: " + exception.getMessage());
			return ERROR;
		}
	}

	private Equation readEquation() {
		int a = readCoefficient("A");
		int b = readCoefficient("B");
		int c = readCoefficient("C");

		return Equation.of(a, b, c);
	}

	private int readCoefficient(String name) {
		out.printf("Введите %s: ", name);

		if (!scanner.hasNextLine()) {
			throw new MathToolException(
					"не удалось прочитать коэффициент " + name
			);
		}

		String value = scanner.nextLine().trim();

		return Equation.parseCoefficient(value, name);
	}

	private void solveAndPrint(Equation equation) {
		EquationSolution solution = equationSolver.solve(equation);
		solutionPrinter.print(solution, out);
	}

	private void printHelp() {
		out.println("""
                mathtool — решение уравнений вида A*x^2 + B*x + C = 0

                Использование:
                  java -jar mathtool.jar
                  java -jar mathtool.jar --help
                  java -jar mathtool.jar solve
                  java -jar mathtool.jar solve -a 1 -b -3 -c 2

                Параметры:
                  -a <число>    коэффициент A
                  -b <число>    коэффициент B
                  -c <число>    коэффициент C

                Коэффициенты A, B, C должны быть целыми числами
                в диапазоне от -100000000 до 100000000 включительно.
                """);
	}
}