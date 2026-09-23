package by.thefirstplacecoder.mathtool.app;

import by.thefirstplacecoder.mathtool.cli.CommandLineParser;
import by.thefirstplacecoder.mathtool.impl.equation.EquationSolver;
import by.thefirstplacecoder.mathtool.impl.equation.SolutionPrinter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MathToolApplicationTest {

    private MathToolApplication createApp(ByteArrayOutputStream out, ByteArrayOutputStream err) {
        return new MathToolApplication(
                new CommandLineParser(),
                new EquationSolver(),
                new SolutionPrinter(),
                new Scanner(""),
                new PrintStream(out),
                new PrintStream(err)
        );
    }

    @Test
    void shouldReadNumbersFromResources() throws Exception {
        URL resource = getClass().getClassLoader().getResource("numbers.txt");
        assertNotNull(resource);

        Path file = Path.of(resource.toURI());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        int code = createApp(out, err).run(new String[]{
                "stats", "--input", file.toString()
        });

        assertEquals(0, code);
        assertTrue(out.toString().contains("Сумма: 10.000"));
        assertTrue(out.toString().contains("Дисперсия: 2.889"));
        assertTrue(err.toString().isEmpty());
    }
}
