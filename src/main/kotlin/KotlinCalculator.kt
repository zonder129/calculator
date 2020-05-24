package org.jetbrains

import java.math.BigDecimal
import java.util.*
import kotlin.IllegalStateException
import kotlin.NoSuchElementException

/*
Create a simple calculator that given a string of operators (), +, -, *, / and numbers separated by spaces
returns the value of that expression

Example: evaluate("1 + 1") => 2
*/
class KotlinCalculator {
    fun evaluate(str: String): Double {
        val expression = str.split(" ").filter { value -> value.isNotBlank() }
        val rpnExpression = transformIntoRPN(expression)
        return calculateInRpn(rpnExpression)
    }

    private fun calculateInRpn(rpnExpression: Deque<String>): Double {
        val tempValues = mutableListOf<String>()
        while (rpnExpression.size != 1 || !isNumber(rpnExpression.first())) {
            val value = rpnExpression.pop()
            if (isOperation(value)) {
                when (tempValues.size) {
                    2 -> {
                        val calculatedValue = applyOperation(tempValues[0], tempValues[1], value)
                        rpnExpression.push(calculatedValue)
                    }
                    1 -> {
                        if (value != "-" && value != "+") throw IllegalStateException("Only + and - allowed as unary operation")
                        val calculatedValue = applyOperation(
                            secondOperand = tempValues[0],
                            operation = value
                        )
                        rpnExpression.push(calculatedValue)
                    }
                    else -> {
                        throw IllegalStateException("More than 2 numbers provided in expression: $tempValues")
                    }
                }
                tempValues.clear()
            } else {
                tempValues.add(value)
            }
        }
        return rpnExpression.first().toDouble()
    }

    private fun applyOperation(firstOperand: String = "0", secondOperand: String, operation: String): String {
        return when (operation) {
            "+" -> {
                firstOperand.toDouble() + secondOperand.toDouble()
            }
            "-" -> {
                firstOperand.toDouble() - secondOperand.toDouble()
            }
            "*" -> {
                firstOperand.toDouble() * secondOperand.toDouble()
            }
            "/" -> {
                firstOperand.toDouble() / secondOperand.toDouble()
            }
            else -> throw IllegalStateException("Unexpected operation: $operation")
        }.toString()
    }

    /**
     * transform expression from infix notation into reverse polish notation
     */
    private fun transformIntoRPN(expression: List<String>): Deque<String> {
        val operationsStack = ArrayDeque<String>()
        val reversePolishNotation = ArrayDeque<String>()

        expression.forEachIndexed { index, value ->
            // TODO: unary minus
            when {
                value == ")" -> {
                    var lastOperation: String? = null
                    while (lastOperation != "(") {
                        lastOperation?.let {
                            reversePolishNotation.add(it)
                        }
                        if (operationsStack.isEmpty()) {
                            throw IllegalStateException("Found undisclosed closing bracket in expression at position $index")
                        }
                        lastOperation = operationsStack.pop()
                    }
                }
                value == "(" -> {
                    operationsStack.push(value)
                }
                isOperation(value) -> {
                    if (operationsStack.isNotEmpty()) {
                        val operationPriority = getOperationPriority(value)
                        val stackOpPriority = getOperationPriority(operationsStack.first)
                        while (operationsStack.isNotEmpty() && operationPriority <= stackOpPriority) {
                            reversePolishNotation.add(operationsStack.pop())
                        }
                    }
                    operationsStack.push(value)
                }
                isNumber(value) -> {
                    reversePolishNotation.add(value)
                }
                else -> {
                    throw IllegalStateException("Unexpected token (neither number or operation) $value at position $index")
                }
            }
        }

        if (operationsStack.contains("(")) {
            throw IllegalStateException("Found undisclosed opening bracket in expression")
        }

        while (operationsStack.isNotEmpty()) {
            reversePolishNotation.add(operationsStack.pop())
        }

        return reversePolishNotation
    }

    private fun isNumber(value: String): Boolean = value.toDoubleOrNull() != null

    private fun isOperation(value: String): Boolean = value in listOf("+", "-", "*", "/", "(")

    private fun getOperationPriority(operation: String): Int {
        return when (operation) {
            "*", "/" -> 3
            "+", "-" -> 2
            "(" -> 1
            else -> throw IllegalStateException("Unexpected operation: $operation")
        }
    }
}



