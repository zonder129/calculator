package org.jetbrains

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource


class KotlinCalculatorTest {
    @ParameterizedTest
    @CsvFileSource(resources = ["/operations_pairwise.csv"], numLinesToSkip = 1)
    @DisplayName("All binary operations should work as expected")
    fun operationCalculation(firstOperand: String, operator: String, secondOperand: String, expected: String) {
        val expression = "$firstOperand $operator $secondOperand"
        assertEquals( expected.toDouble(), evaluate(expression), 1e-4)
    }
}