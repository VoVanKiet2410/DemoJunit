package com.junitTest.testing.TestSuites;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MultiplicationTest {
    @Test
    void testMultiplication() {
        assertEquals(6, 2 * 3);
    }
}
