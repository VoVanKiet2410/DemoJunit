package com.junitTest.testing.TestSuites;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SubtractionTest {
    @Test
    void testSubtraction() {
        assertEquals(10,  20 - 10);
    }
}
