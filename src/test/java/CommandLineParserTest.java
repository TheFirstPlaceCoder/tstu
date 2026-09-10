import by.thefirstplacecoder.mathtool.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineParserTest {

	private final CommandLineParser parser =
			new CommandLineParser();

	@Test
	void shouldReturnHelpWhenNoArgumentsProvided() {
		CommandLineParser.Command command =
				parser.parse(new String[]{});

		assertInstanceOf(
				CommandLineParser.HelpCommand.class,
				command
		);
	}

	@Test
	void shouldReturnHelpForHelpArgument() {
		CommandLineParser.Command command =
				parser.parse(new String[]{"--help"});

		assertInstanceOf(
				CommandLineParser.HelpCommand.class,
				command
		);
	}

	@Test
	void shouldEnableInteractiveModeForSolveCommand() {
		CommandLineParser.Command command =
				parser.parse(new String[]{"solve"});

		assertInstanceOf(
				CommandLineParser.InteractiveSolveCommand.class,
				command
		);
	}

	@Test
	void shouldParseEquationFromArguments() {
		String[] args = {
				"solve",
				"-a", "1",
				"-b", "-3",
				"-c", "2"
		};

		CommandLineParser.Command command =
				parser.parse(args);

		assertInstanceOf(
				CommandLineParser.SolveCommand.class,
				command
		);

		CommandLineParser.SolveCommand solveCommand =
				(CommandLineParser.SolveCommand) command;

		assertEquals(
				Equation.of(1, -3, 2),
				solveCommand.equation()
		);
	}

	@Test
	void shouldAllowArgumentsInDifferentOrder() {
		String[] args = {
				"solve",
				"-c", "2",
				"-a", "1",
				"-b", "-3"
		};

		CommandLineParser.SolveCommand command =
				(CommandLineParser.SolveCommand)
						parser.parse(args);

		assertEquals(
				Equation.of(1, -3, 2),
				command.equation()
		);
	}

	@Test
	void shouldRejectUnknownCommand() {
		MathToolException exception = assertThrows(
				MathToolException.class,
				() -> parser.parse(
						new String[]{"destroy-production"}
				)
		);

		assertTrue(
				exception.getMessage()
				         .contains("неизвестная команда")
		);
	}

	@Test
	void shouldRejectUnknownParameter() {
		String[] args = {
				"solve",
				"-a", "1",
				"-b", "2",
				"--database-password", "123"
		};

		assertThrows(
				MathToolException.class,
				() -> parser.parse(args)
		);
	}

	@Test
	void shouldRejectDuplicateParameter() {
		String[] args = {
				"solve",
				"-a", "1",
				"-a", "2",
				"-c", "3"
		};

		MathToolException exception = assertThrows(
				MathToolException.class,
				() -> parser.parse(args)
		);

		assertTrue(
				exception.getMessage()
				         .contains("несколько раз")
		);
	}

	@Test
	void shouldRejectMissingParameters() {
		String[] args = {
				"solve",
				"-a", "1",
				"-b", "2"
		};

		assertThrows(
				MathToolException.class,
				() -> parser.parse(args)
		);
	}
}