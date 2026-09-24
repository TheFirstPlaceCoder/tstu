package by.thefirstplacecoder.mathtool.impl.integration;

import by.thefirstplacecoder.mathtool.common.MathToolException;
import by.thefirstplacecoder.mathtool.cli.CliException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class IntegrationCalculator {

    public static final int MAX_STEPS = 100_000;

    public record FunctionDefinition(
            DoubleUnaryOperator function,
            String formula,
            double low,
            double high,
            boolean boundsInclusive
    ) {
    }

    private static final Map<String, FunctionDefinition> FUNCTIONS = createFunctions();

    public static double ratio(double x) {
        return x / (x + 1.0);
    }

    public static double root(double x) {
        return Math.sqrt(x * x + 1.0);
    }

    public static double integrate(DoubleUnaryOperator function, double from, double to, int steps) {
        if (steps < 1 || steps > MAX_STEPS) {
            throw new MathToolException("количество шагов вне диапазона");
        }

        double dx = (to - from) / steps;
        double result = 0.0;

        for (int i = 0; i < steps; i++) {
            double x = from + i * dx;
            result += function.applyAsDouble(x) * dx;
        }

        return result;
    }

    public static void validate(FunctionDefinition definition, double from, double to, int steps) {
        if (!Double.isFinite(from) || !Double.isFinite(to)) {
            throw new MathToolException("предел интегрирования должен быть конечным числом");
        }

        if (from >= to) {
            throw new MathToolException("начальный предел не меньше конечного");
        }

        if (steps < 1 || steps > MAX_STEPS) {
            throw new MathToolException("количество шагов вне диапазона");
        }

        validateBound(definition, from);
        validateBound(definition, to);
    }

    public static FunctionDefinition getDefinition(String name) {
        FunctionDefinition definition = FUNCTIONS.get(name);

        if (definition == null) {
            throw new CliException("неизвестная функция: " + name + "; допустимо: " + FUNCTIONS.keySet());
        }

        return definition;
    }

    private static void validateBound(FunctionDefinition definition, double value) {
        boolean outside = definition.boundsInclusive()
                ? value < definition.low() || value > definition.high()
                : value <= definition.low() || value >= definition.high();

        if (outside) {
            throw new MathToolException("предел вне промежутка функции");
        }
    }

    private static Map<String, FunctionDefinition> createFunctions() {
        Map<String, FunctionDefinition> result = new LinkedHashMap<>();

        result.put("ratio", new FunctionDefinition(
                IntegrationCalculator::ratio,
                "F(x) = x / (x + 1)",
                0.0,
                20.0,
                true
        ));

        result.put("root", new FunctionDefinition(
                IntegrationCalculator::root,
                "F(x) = sqrt(x^2 + 1)",
                -5.0,
                5.0,
                false
        ));

        return Map.copyOf(result);
    }
}
