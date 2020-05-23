package org.jetbrains

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class KotlinCalculatorTest {
    @Test
    fun simpleTest() {
        assertEquals( 2.0, evaluate("1 + 1"), 1e-4, "simple addition")
    }
}