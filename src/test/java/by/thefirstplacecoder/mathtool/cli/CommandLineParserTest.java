package by.thefirstplacecoder.mathtool.cli;

import by.thefirstplacecoder.mathtool.impl.equation.Equation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineParserTest {

    private final CommandLineParser parser = new CommandLineParser();

    @Test
    void shouldParseSolve() {
        var command = (CommandLineParser.SolveCommand) parser.parse(
                new String[]{"solve", "-a", "1", "-b", "-3", "-c", "2"}
        );

        assertEquals(Equation.of(1, -3, 2), command.equation());
    }

    @Test
    void shouldRejectUnknownCommand() {
        assertThrows(CliException.class, () -> parser.parse(new String[]{"abc"}));
    }

    @Test
    void shouldParseSeries() {
        var command = (CommandLineParser.SeriesCommand) parser.parse(
                new String[]{"series", "--func", "sqplus", "--terms", "6"}
        );

        assertEquals("sqplus", command.func());
        assertEquals(6, command.terms());
    }
}
