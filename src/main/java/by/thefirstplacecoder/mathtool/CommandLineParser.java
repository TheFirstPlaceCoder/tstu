package by.thefirstplacecoder.mathtool;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CommandLineParser {

	private static final String SOLVE_COMMAND = "solve";

	private static final Set<String> SUPPORTED_OPTIONS =
			Set.of("-a", "-b", "-c");

	public Command parse(String[] args) {
		if (args == null || args.length == 0) {
			return new HelpCommand();
		}

		if (args.length == 1 && "--help".equals(args[0])) {
			return new HelpCommand();
		}

		if (!SOLVE_COMMAND.equals(args[0])) {
			throw new MathToolException(
					"неизвестная команда: " + args[0]
			);
		}

		if (args.length == 1) {
			return new InteractiveSolveCommand();
		}

		return parseSolveCommand(args);
	}

	private SolveCommand parseSolveCommand(String[] args) {
		if (args.length != 7) {
			throw new MathToolException(
					"неверный набор параметров"
			);
		}

		Map<String, String> options = new HashMap<>();

		for (int i = 1; i < args.length; i += 2) {
			String option = args[i];
			String value = args[i + 1];

			validateOption(option);

			if (options.putIfAbsent(option, value) != null) {
				throw new MathToolException(
						"параметр указан несколько раз: " + option
				);
			}
		}

		ensureRequiredOptionsPresent(options);

		int a = Equation.parseCoefficient(options.get("-a"), "A");
		int b = Equation.parseCoefficient(options.get("-b"), "B");
		int c = Equation.parseCoefficient(options.get("-c"), "C");

		return new SolveCommand(Equation.of(a, b, c));
	}

	private void validateOption(String option) {
		if (!SUPPORTED_OPTIONS.contains(option)) {
			throw new MathToolException(
					"неизвестный параметр: " + option
			);
		}
	}

	private void ensureRequiredOptionsPresent(Map<String, String> options) {
		for (String requiredOption : SUPPORTED_OPTIONS) {
			if (!options.containsKey(requiredOption)) {
				throw new MathToolException(
						"отсутствует обязательный параметр: "
								+ requiredOption
				);
			}
		}
	}

	public sealed interface Command
			permits HelpCommand,
			InteractiveSolveCommand,
			SolveCommand {
	}

	public record HelpCommand() implements Command {
	}

	public record InteractiveSolveCommand() implements Command {
	}

	public record SolveCommand(
			Equation equation
	) implements Command {

		public SolveCommand {
			if (equation == null) {
				throw new IllegalArgumentException(
						"выражение не должно быть null"
				);
			}
		}
	}
}