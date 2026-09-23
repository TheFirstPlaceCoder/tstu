package by.thefirstplacecoder.mathtool.series;

import by.thefirstplacecoder.mathtool.impl.series.SeriesCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesCalculatorTest {

    @Test
    void shouldCalculateSixTerms() {
        var result = SeriesCalculator.sumByTerms(SeriesCalculator::sqplusTerm, 6);

        assertEquals(6, result.terms());
        assertEquals(0.3526, result.sum(), 0.00005);
    }

    @Test
    void shouldStopByEps() {
        var result = SeriesCalculator.sumByEps(SeriesCalculator::sqplusTerm, 0.0001);

        assertEquals(100, result.terms());
        assertEquals(0.3639, result.sum(), 0.00005);
    }
}
