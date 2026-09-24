package by.thefirstplacecoder.mathtool.app;

import by.thefirstplacecoder.mathtool.cli.CliException;
import by.thefirstplacecoder.mathtool.cli.CommandLineParser;
import by.thefirstplacecoder.mathtool.common.MathToolException;
import by.thefirstplacecoder.mathtool.impl.equation.Equation;
import by.thefirstplacecoder.mathtool.impl.equation.EquationSolver;
import by.thefirstplacecoder.mathtool.impl.equation.SolutionPrinter;
import by.thefirstplacecoder.mathtool.impl.integration.IntegrationCalculator;
import by.thefirstplacecoder.mathtool.impl.series.SeriesCalculator;
import by.thefirstplacecoder.mathtool.impl.stats.StatisticsCalculator;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MathToolApplication {

    private final CommandLineParser parser;
    private final EquationSolver equationSolver;
    private final SolutionPrinter solutionPrinter;
    private final Scanner scanner;
    private final PrintStream out;
    private final PrintStream err;

    public MathToolApplication() {
        this(new CommandLineParser(), new EquationSolver(), new SolutionPrinter(),
                new Scanner(System.in), System.out, System.err);
    }

    public MathToolApplication(CommandLineParser parser, EquationSolver equationSolver,
                               SolutionPrinter solutionPrinter, Scanner scanner,
                               PrintStream out, PrintStream err) {
        this.parser = parser;
        this.equationSolver = equationSolver;
        this.solutionPrinter = solutionPrinter;
        this.scanner = scanner;
        this.out = out;
        this.err = err;
    }

    public int run(String[] args) {
        try {
            CommandLineParser.Command command = parser.parse(args);

            if (command instanceof CommandLineParser.HelpCommand help) {
                printHelp(help.command());
                return 0;
            }

            if (command instanceof CommandLineParser.InteractiveSolveCommand) {
                solve(readEquation());
                return 0;
            }

            if (command instanceof CommandLineParser.SolveCommand solve) {
                solve(solve.equation());
                return 0;
            }

            if (command instanceof CommandLineParser.StatsCommand stats) {
                runStats(stats.input());
                return 0;
            }

            if (command instanceof CommandLineParser.SeriesCommand series) {
                runSeries(series);
                return 0;
            }

            CommandLineParser.IntegrateCommand integrate =
                    (CommandLineParser.IntegrateCommand) command;
            runIntegrate(integrate);
            return 0;

        } catch (CliException e) {
            err.println("ОШИБКА CLI: " + e.getMessage());
            return 2;
        } catch (MathToolException e) {
            err.println("ОШИБКА: " + e.getMessage());
            return 1;
        }
    }

    private void solve(Equation equation) {
        solutionPrinter.print(equationSolver.solve(equation), out);
    }

    private Equation readEquation() {
        int a = readCoefficient("A");
        int b = readCoefficient("B");
        int c = readCoefficient("C");

        return Equation.of(a, b, c);
    }

    private int readCoefficient(String name) {
        out.print("Введите " + name + ": ");

        if (!scanner.hasNextLine()) {
            throw new MathToolException("не удалось прочитать коэффициент " + name);
        }

        return Equation.parseCoefficient(scanner.nextLine(), name);
    }

    private void runStats(Path input) {
        double[] values = input == null ? readNumbersFromScanner() : readNumbersFromFile(input);
        StatisticsCalculator.validate(values);

        out.println("Количество: " + values.length);
        printNumber("Сумма", StatisticsCalculator.sum(values), 3);
        printNumber("Ср. арифм.", StatisticsCalculator.mean(values), 3);
        printNumber("Сумма кв.", StatisticsCalculator.sumOfSquares(values), 3);
        printNumber("Ср. кв.", StatisticsCalculator.rootMeanSquare(values), 3);
        printNumber("Дисперсия", StatisticsCalculator.variance(values), 3);
        printNumber("СКО", StatisticsCalculator.standardDeviationPopulation(values), 3);

        Double sample = StatisticsCalculator.standardDeviationSample(values);
        if (sample == null) {
            out.println("Станд. откл.: НЕ СУЩЕСТВУЕТ");
        } else {
            printNumber("Станд. откл.", sample, 3);
        }

        printNumber("Наименьшее", StatisticsCalculator.min(values), 3);
        printNumber("Наибольшее", StatisticsCalculator.max(values), 3);
        out.println("Положительных: " + StatisticsCalculator.positiveCount(values));
        out.println("Отрицательных: " + StatisticsCalculator.negativeCount(values));
    }

    private double[] readNumbersFromFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            List<Double> values = new ArrayList<>();

            for (String line : lines) {
                addNumbers(line, values);
            }

            return toArray(values);
        } catch (IOException e) {
            throw new MathToolException("не удалось прочитать файл: " + path);
        }
    }

    private double[] readNumbersFromScanner() {
        List<Double> values = new ArrayList<>();

        String line = "";
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).isBlank()) {
            addNumbers(line, values);
        }

        return toArray(values);
    }

    private void addNumbers(String line, List<Double> values) {
        String text = line.trim();

        if (text.isEmpty()) {
            return;
        }

        for (String part : text.split("\\s+")) {
            try {
                values.add(Double.parseDouble(part));
            } catch (NumberFormatException e) {
                throw new MathToolException(part + " не является числом");
            }
        }
    }

    // костыли
    private double[] toArray(List<Double> values) {
        double[] result = new double[values.size()];

        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }

        return result;
    }

    private void runSeries(CommandLineParser.SeriesCommand command) {
        SeriesCalculator.SeriesDefinition series =
                SeriesCalculator.getDefinition(command.func());

        SeriesCalculator.SeriesResult result;

        if (command.terms() != null) {
            result = SeriesCalculator.sumByTerms(series.term(), command.terms());
        } else {
            result = SeriesCalculator.sumByEps(series.term(), command.eps());
        }

        out.println(series.formula());
        out.println("Слагаемых: " + result.terms());
        printNumber("Сумма ряда", result.sum(), 4);
    }

    private void runIntegrate(CommandLineParser.IntegrateCommand command) {
        IntegrationCalculator.FunctionDefinition function =
                IntegrationCalculator.getDefinition(command.func());

        IntegrationCalculator.validate(function, command.from(), command.to(), command.steps());
        double result = IntegrationCalculator.integrate(
                function.function(), command.from(), command.to(), command.steps());

        out.println(function.formula());
        printNumber("Значение интеграла", result, 4);
    }

    private void printNumber(String name, double value, int digits) {
        out.printf(Locale.ROOT, "%s: %." + digits + "f%n", name, value == 0.0 ? 0.0 : value);
    }

    private void printHelp(String command) {
        if (command == null) {
            out.println("mathtool");
            out.println("Команды: solve, stats, series, integrate");
            out.println("Для справки: <команда> --help");
            return;
        }

        switch (command) {
            case "solve" -> out.println("solve [-a A -b B -c C], коэффициенты от -10000 до 10000");
            case "stats" -> out.println("stats [--input файл]");
            case "series" -> out.println("series --func third|sqplus (--terms N | --eps E)");
            case "integrate" -> out.println("integrate --func ratio|root --from A --to B --steps N");
        }
    }
}
