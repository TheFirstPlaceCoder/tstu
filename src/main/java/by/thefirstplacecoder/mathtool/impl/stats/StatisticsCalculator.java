package by.thefirstplacecoder.mathtool.impl.stats;

import by.thefirstplacecoder.mathtool.common.MathToolException;

public final class StatisticsCalculator {

    public static final int MAX_VALUES = 20;
    public static final double MAX_ABS_VALUE = 10_000.0;

    public static void validate(double[] values) {
        if (values == null || values.length == 0) {
            throw new MathToolException("последовательность пуста");
        }

        if (values.length > MAX_VALUES) {
            throw new MathToolException("в последовательности больше 20 чисел");
        }

        for (double value : values) {
            if (!Double.isFinite(value)) {
                throw new MathToolException("последовательность содержит не конечное число");
            }

            if (Math.abs(value) > MAX_ABS_VALUE) {
                throw new MathToolException("число находится вне допустимого диапазона [-10000; 10000]");
            }
        }
    }

    public static double sum(double[] values) {
        double result = 0.0;

        for (double value : values) {
            result += value;
        }

        return result;
    }

    public static double mean(double[] values) {
        return sum(values) / values.length;
    }

    public static double sumOfSquares(double[] values) {
        double result = 0.0;

        for (double value : values) {
            result += value * value;
        }

        return result;
    }

    public static double rootMeanSquare(double[] values) {
        return Math.sqrt(sumOfSquares(values) / values.length);
    }

    public static double variance(double[] values) {
        double average = mean(values);
        double result = 0.0;

        for (double value : values) {
            double delta = value - average;

            result += delta * delta;
        }

        return result / values.length;
    }

    public static double standardDeviationPopulation(double[] values) {
        return Math.sqrt(variance(values));
    }

    public static Double standardDeviationSample(double[] values) {
        if (values.length < 2) {
            return null;
        }

        double average = mean(values);
        double result = 0.0;

        for (double value : values) {
            double delta = value - average;

            result += delta * delta;
        }

        return Math.sqrt(result / (values.length - 1));
    }

    public static double min(double[] values) {
        double result = values[0];

        for (double value : values) {
            if (value < result) {
                result = value;
            }
        }

        return result;
    }

    public static double max(double[] values) {
        double result = values[0];

        for (double value : values) {
            if (value > result) {
                result = value;
            }
        }

        return result;
    }

    public static int positiveCount(double[] values) {
        int result = 0;

        for (double value : values) {
            if (value > 0) {
                result++;
            }
        }

        return result;
    }

    public static int negativeCount(double[] values) {
        int result = 0;

        for (double value : values) {
            if (value < 0) {
                result++;
            }
        }

        return result;
    }
}
