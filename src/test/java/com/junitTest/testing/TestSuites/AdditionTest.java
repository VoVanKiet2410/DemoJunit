package com.junitTest.testing.TestSuites;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AdditionTest {
    @Test
    void testAddition() {
        assertEquals(4, 2 + 2);
    }
}
