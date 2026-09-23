package by.thefirstplacecoder.mathtool.impl.series;

import by.thefirstplacecoder.mathtool.common.MathToolException;
import by.thefirstplacecoder.mathtool.cli.CliException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntToDoubleFunction;

public class SeriesCalculator {

    public static final int MAX_ITERATIONS = 100_000;
    public static final int MAX_TERMS = 10_000;
    public static final double MAX_EPS = 0.0001;

    public record SeriesDefinition(IntToDoubleFunction term, String formula) {
    }

    public record SeriesResult(double sum, int terms) {
    }

    private static final Map<String, SeriesDefinition> SERIES = createSeries();

    private SeriesCalculator() {
    }

    public static int sign(int n) {
        return n % 2 == 0 ? -1 : 1;
    }

    public static double thirdTerm(int n) {
        return (double) sign(n) / (3.0 * n);
    }

    public static double sqplusTerm(int n) {
        return (double) sign(n) / ((double) n * n + 1.0);
    }

    public static SeriesResult sumByTerms(IntToDoubleFunction term, int count) {
        if (count < 1 || count > MAX_TERMS) {
            throw new MathToolException("количество слагаемых вне диапазона");
        }

        double result = 0.0;
        for (int n = 1; n <= count; n++) {
            result += term.applyAsDouble(n);
        }

        return new SeriesResult(result, count);
    }

    public static SeriesResult sumByEps(IntToDoubleFunction term, double eps) {
        if (!Double.isFinite(eps) || eps <= 0.0 || eps > MAX_EPS) {
            throw new MathToolException("точность вне диапазона");
        }

        double result = 0.0;
        for (int n = 1; n <= MAX_ITERATIONS; n++) {
            double value = term.applyAsDouble(n);
            result += value;

            if (Math.abs(value) < eps) {
                return new SeriesResult(result, n);
            }
        }

        throw new MathToolException("точность не достигнута");
    }

    public static SeriesDefinition getDefinition(String name) {
        SeriesDefinition definition = SERIES.get(name);

        if (definition == null) {
            throw new CliException("неизвестный ряд: " + name + "; допустимо: " + SERIES.keySet());
        }

        return definition;
    }

    private static Map<String, SeriesDefinition> createSeries() {
        Map<String, SeriesDefinition> result = new LinkedHashMap<>();

        result.put("third", new SeriesDefinition(
                SeriesCalculator::thirdTerm,
                "S = 1/3 - 1/6 + 1/9 - 1/12 + ..."
        ));

        result.put("sqplus", new SeriesDefinition(
                SeriesCalculator::sqplusTerm,
                "S = 1/(1^2+1) - 1/(2^2+1) + 1/(3^2+1) - ..."
        ));

        return Map.copyOf(result);
    }
}
