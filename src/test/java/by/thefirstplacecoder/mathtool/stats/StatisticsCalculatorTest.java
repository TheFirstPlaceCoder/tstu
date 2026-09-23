package by.thefirstplacecoder.mathtool.stats;

import by.thefirstplacecoder.mathtool.impl.stats.StatisticsCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsCalculatorTest {

    private final double[] values = {4, 1, 5};

    @Test
    void shouldCalculateStatistics() {
        assertEquals(10.0, StatisticsCalculator.sum(values), 0.0001);
        assertEquals(10.0 / 3.0, StatisticsCalculator.mean(values), 0.0001);
        assertEquals(42.0, StatisticsCalculator.sumOfSquares(values), 0.0001);
        assertEquals(1.0, StatisticsCalculator.min(values), 0.0001);
        assertEquals(5.0, StatisticsCalculator.max(values), 0.0001);
    }
}
