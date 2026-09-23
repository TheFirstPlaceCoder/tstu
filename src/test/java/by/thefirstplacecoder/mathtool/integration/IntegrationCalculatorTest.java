package by.thefirstplacecoder.mathtool.integration;

import by.thefirstplacecoder.mathtool.impl.integration.IntegrationCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationCalculatorTest {

    @Test
    void shouldIntegrateRatio() {
        double result = IntegrationCalculator.integrate(
                IntegrationCalculator::ratio, 0, 20, 1000
        );

        assertEquals(16.9459, result, 0.00005);
    }
}
