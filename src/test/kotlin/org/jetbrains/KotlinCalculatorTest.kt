package org.jetbrains

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class KotlinCalculatorTest {

    val calcDelta = 1e-4

    @Nested
    inner class OperationsCalculation {

        @ParameterizedTest
        @CsvFileSource(resources = ["/operations_pairwise.csv"], numLinesToSkip = 1)
        @DisplayName("All binary operations should be calculated properly")
        fun operationCalculation(
            firstOperand: String,
            operator: String,
            secondOperand: String,
            expected: String
        ) {
            val expression = "$firstOperand $operator $secondOperand"
            LOG.info("Expression used: $expression")
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(expected.toDouble(), actual, calcDelta, "Calculation is not as expected")
        }

        @Test
        @DisplayName("Unary minus before number is calculated properly")
        fun unaryMinus() {
            val expression = "- 5 + 3"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(-2.0, actual, calcDelta, "Numbers are not the same")
        }

        @Test
        @DisplayName("Unary minus before bracket is calculated properly")
        fun unaryMinusBeforeBracket() {
            val expression = "- ( 1 - 3 )"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(2.0, actual, calcDelta, "Numbers are not the same")
        }

        @Test
        @DisplayName("Unary plus before number is calculated properly")
        fun unaryPlusBeforeNumber() {
            val expression = "+ 5 + 3"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(8.0, actual, calcDelta, "Numbers are not the same")
        }

        @Test
        @DisplayName("Unary plus before bracket is calculated properly")
        fun unaryPlusBeforeBracket() {
            val expression = "+ ( 5 - 3 )"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(2.0, actual, calcDelta, "Numbers are not the same")
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/priorities_pairwise.csv"], numLinesToSkip = 1)
    @DisplayName("All priorities of binary operations should be calculated properly")
    fun prioritiesCalculation(
        firstOperand: String,
        firstOperator: String,
        secondOperand: String,
        secondOperator: String,
        thirdOperand: String,
        isBrackets: String,
        expected: String
    ) {
        val expression = if (isBrackets.toBoolean()) {
            "( $firstOperand $firstOperator $secondOperand ) $secondOperator $thirdOperand"
        } else {
            "$firstOperand $firstOperator $secondOperand $secondOperator $thirdOperand"
        }
        LOG.info("Expression used: $expression")
        val actual = KotlinCalculator().evaluate(expression)
        assertEquals(expected.toDouble(), actual, calcDelta, "Calculation is not as expected")
    }

    @Nested
    inner class ExceptionalCases {
        @Test
        @DisplayName("Very large positive numbers (more than Double) results into infinity")
        fun biggestPositiveNumber() {
            val expression = "${Double.MAX_VALUE} + ${Double.MAX_VALUE}"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(Double.POSITIVE_INFINITY, actual, "Positive infinity expected")
        }

        @Test
        @DisplayName("Very large negative numbers (more than Double) results into -infinity")
        fun biggestNegativeNumber() {
            val expression = "-${Double.MAX_VALUE} - ${Double.MAX_VALUE} "
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(Double.NEGATIVE_INFINITY, actual, "Negative infinity expected")
        }

        @Test
        @DisplayName("Very small positive fraction results into zero")
        fun minimumFractionNumber() {
            val expression = "${Double.MIN_VALUE} * 0.1"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(0.0, actual, "Zero expected")
        }

        @Test
        @DisplayName("Not a numbers can't be processed")
        fun notANumber() {
            val expression = "foo + bar"
            val ex = assertThrows(IllegalStateException::class.java) {
                KotlinCalculator().evaluate(expression)
            }
            LOG.info("Exception caught: ${ex.message}")
        }

        @Test
        @DisplayName("Not an operation can't be processed")
        fun notAnOperation() {
            val expression = "10 versus 5"
            val ex = assertThrows(IllegalStateException::class.java) {
                KotlinCalculator().evaluate(expression)
            }
            LOG.info("Exception caught: ${ex.message}")
        }

        @Test
        @DisplayName("Comma as fraction symbol can't be processed")
        fun fractionComma() {
            val expression = "10,5 - 5,3"
            val ex = assertThrows(IllegalStateException::class.java) {
                KotlinCalculator().evaluate(expression)
            }
            LOG.info("Exception caught: ${ex.message}")
        }

        @Test
        @DisplayName("Double fraction dot symbol can't be processed")
        fun doubleFractionDot() {
            val expression = "10..5 - 5.3"
            val ex = assertThrows(IllegalStateException::class.java) {
                KotlinCalculator().evaluate(expression)
            }
            LOG.info("Exception caught: ${ex.message}")
        }

        @Test
        @DisplayName("Common fractions can't be processed")
        fun commonFraction() {
            val expression = "3/4 - 1/2"
            val ex = assertThrows(IllegalStateException::class.java) {
                KotlinCalculator().evaluate(expression)
            }
            LOG.info("Exception caught: ${ex.message}")
        }

        @Test
        @DisplayName("Numbers with leading zeros are treated as usual numbers")
        fun leadingZeros() {
            val expression = "5 + 0003"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(8.0, actual, calcDelta, "Numbers are not the same")
        }

        @Test
        @DisplayName("Expression with missing opening bracket can't be processed")
        fun missingOpeningBracket() {
            val expression = "7 + 10 )"
            val ex = assertThrows(IllegalStateException::class.java) {
                KotlinCalculator().evaluate(expression)
            }
            LOG.info("Exception caught: ${ex.message}")
        }

        @Test
        @DisplayName("Expression with missing closing bracket can't be processed")
        fun missingClosingBracket() {
            val expression = "( 7 + 10"
            val ex = assertThrows(IllegalStateException::class.java) {
                KotlinCalculator().evaluate(expression)
            }
            LOG.info("Exception caught: ${ex.message}")
        }

        @Test
        @DisplayName("Expression with extra spaces is calculated properly")
        fun extraSpaces() {
            val expression = "15  + 12 "
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(27.0, actual, calcDelta, "Numbers are not the same")
        }

        @Test
        @DisplayName("Division by zero returns infinity")
        fun divisionByZero() {
            val expression = "10 / 0"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(Double.POSITIVE_INFINITY, actual, calcDelta, "Positive infinity expected")
        }

        @Test
        @DisplayName("Division fraction by zero returns infinity")
        fun divisionFractionByZero() {
            val expression = "10.0 / 0"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(Double.POSITIVE_INFINITY, actual, calcDelta, "Positive infinity expected")
        }

        @Test
        @DisplayName("Division zero by zero returns NaN")
        fun divisionZeroByZero() {
            val expression = "0 / 0"
            val actual = KotlinCalculator().evaluate(expression)
            assertEquals(Double.NaN, actual, calcDelta, "NaN expected")
        }
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(KotlinCalculatorTest::class.java)
    }
}