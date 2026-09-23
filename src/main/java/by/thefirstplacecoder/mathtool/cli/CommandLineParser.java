package by.thefirstplacecoder.mathtool.cli;

import by.thefirstplacecoder.mathtool.impl.equation.Equation;
import by.thefirstplacecoder.mathtool.impl.integration.IntegrationCalculator;
import by.thefirstplacecoder.mathtool.impl.series.SeriesCalculator;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandLineParser {

    public Command parse(String[] args) {
        if (args == null || args.length == 0) {
            return new HelpCommand(null);
        }

        if (args.length == 1 && args[0].equals("--help")) {
            return new HelpCommand(null);
        }

        String command = args[0];

        if (!Set.of("solve", "stats", "series", "integrate").contains(command)) {
            throw new CliException("неизвестная команда: " + command);
        }

        if (args.length == 2 && args[1].equals("--help")) {
            return new HelpCommand(command);
        }

        return switch (command) {
            case "solve" -> parseSolve(args);
            case "stats" -> parseStats(args);
            case "series" -> parseSeries(args);
            case "integrate" -> parseIntegrate(args);
            default -> throw new CliException("неизвестная команда");
        };
    }

    private Command parseSolve(String[] args) {
        if (args.length == 1) {
            return new InteractiveSolveCommand();
        }

        Map<String, String> p = readOptions(args, Set.of("-a", "-b", "-c"));

        if (!p.containsKey("-a") || !p.containsKey("-b") || !p.containsKey("-c")) {
            throw new CliException("нужно указать -a, -b и -c");
        }

        return new SolveCommand(Equation.of(
                parseInt(p.get("-a"), "-a"),
                parseInt(p.get("-b"), "-b"),
                parseInt(p.get("-c"), "-c")
        ));
    }

    private Command parseStats(String[] args) {
        if (args.length == 1) {
            return new StatsCommand(null);
        }

        Map<String, String> p = readOptions(args, Set.of("--input"));
        String file = p.get("--input");
        return new StatsCommand(file == null ? null : Path.of(file));
    }

    private Command parseSeries(String[] args) {
        Map<String, String> p = readOptions(args, Set.of("--func", "--terms", "--eps"));
        require(p, "--func");

        boolean terms = p.containsKey("--terms");
        boolean eps = p.containsKey("--eps");

        if (terms == eps) {
            throw new CliException("нужно указать только один параметр: --terms или --eps");
        }

        SeriesCalculator.getDefinition(p.get("--func"));

        if (terms) {
            return new SeriesCommand(p.get("--func"), parseInt(p.get("--terms"), "--terms"), null);
        }

        return new SeriesCommand(p.get("--func"), null, parseDouble(p.get("--eps"), "--eps"));
    }

    private Command parseIntegrate(String[] args) {
        Map<String, String> p = readOptions(
                args,
                Set.of("--func", "--from", "--to", "--steps")
        );

        require(p, "--func");
        require(p, "--from");
        require(p, "--to");
        require(p, "--steps");

        IntegrationCalculator.getDefinition(p.get("--func"));

        return new IntegrateCommand(
                p.get("--func"),
                parseDouble(p.get("--from"), "--from"),
                parseDouble(p.get("--to"), "--to"),
                parseInt(p.get("--steps"), "--steps")
        );
    }

    private Map<String, String> readOptions(String[] args, Set<String> allowed) {
        Map<String, String> result = new HashMap<>();

        for (int i = 1; i < args.length; i += 2) {
            String name = args[i];

            if (!allowed.contains(name)) {
                throw new CliException("неизвестный параметр: " + name);
            }

            if (i + 1 >= args.length) {
                throw new CliException("нет значения для " + name);
            }

            if (result.containsKey(name)) {
                throw new CliException("параметр указан несколько раз: " + name);
            }

            result.put(name, args[i + 1]);
        }

        return result;
    }

    private void require(Map<String, String> p, String name) {
        if (!p.containsKey(name)) {
            throw new CliException("отсутствует параметр " + name);
        }
    }

    private int parseInt(String value, String name) {
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            throw new CliException(name + " должен быть целым числом");
        }
    }

    private double parseDouble(String value, String name) {
        try {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e) {
            throw new CliException(name + " должен быть числом");
        }
    }

    public sealed interface Command permits HelpCommand, InteractiveSolveCommand, SolveCommand,
            StatsCommand, SeriesCommand, IntegrateCommand {
    }

    public record HelpCommand(String command) implements Command {
    }

    public record InteractiveSolveCommand() implements Command {
    }

    public record SolveCommand(Equation equation) implements Command {
    }

    public record StatsCommand(Path input) implements Command {
    }

    public record SeriesCommand(
            String func,
            Integer terms,
            Double eps
    ) implements Command {
    }

    public record IntegrateCommand(
            String func,
            double from,
            double to,
            int steps
    ) implements Command {
    }
}
