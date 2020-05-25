package org.jetbrains

import java.util.*
import kotlin.IllegalStateException

/*
Create a simple calculator that given a string of operators (), +, -, *, / and numbers separated by spaces
returns the value of that expression

Example: evaluate("1 + 1") => 2
*/
class KotlinCalculator {

    private val operations = listOf("+", "-", "*", "/", "(")

    fun evaluate(str: String): Double {
        val expression = str.split(" ").filter { value -> value.isNotBlank() }
        return calculateWithRPN(expression).first().toDouble()
    }

    /**
     * calculating expression by transforming it from infix notation into reverse polish notation
     */
    private fun calculateWithRPN(expression: List<String>): Deque<String> {
        val operationsStack = ArrayDeque<String>()
        val calculations = ArrayDeque<String>()

        expression.forEachIndexed { index, value ->
            when {
                value == ")" -> {
                    var lastOperation: String? = null
                    while (lastOperation != "(") {
                        lastOperation?.let {
                            processOperation(calculations, it)
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
                    val currOp = if (isUnary(value, expression.getOrNull(index-1))) "u$value" else value
                    if (operationsStack.isNotEmpty()) {
                        val operationPriority = getOperationPriority(currOp)
                        val stackOpPriority = getOperationPriority(operationsStack.first)
                        while (operationsStack.isNotEmpty() && operationPriority <= stackOpPriority) {
                            processOperation(calculations, operationsStack.pop())
                        }
                    }
                    operationsStack.push(currOp)
                }
                isNumber(value) -> {
                    calculations.push(value)
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
            processOperation(calculations, operationsStack.pop())
        }

        return calculations
    }

    private fun processOperation(calculations: Deque<String>, operation: String) {
        val isUnaryOp = operation.contains("u")
        val realOperation = if (isUnaryOp) operation.last().toString() else operation
        if (!isUnaryOp && calculations.size < 2 || calculations.isEmpty()) {
            throw IllegalStateException("No numbers to calculate with operation $realOperation")
        }
        val firstArg = calculations.pop()
        val secondArg = if (isUnaryOp) "0" else calculations.pop()
        val calculation = applyOperation(firstArg, secondArg, realOperation)
        calculations.push(calculation)
    }

    private fun applyOperation(firstOperand: String, secondOperand: String, operation: String): String {
        return when (operation) {
            "+" -> {
                secondOperand.toDouble() + firstOperand.toDouble()
            }
            "-" -> {
                secondOperand.toDouble() - firstOperand.toDouble()
            }
            "*" -> {
                secondOperand.toDouble() * firstOperand.toDouble()
            }
            "/" -> {
                secondOperand.toDouble() / firstOperand.toDouble()
            }
            else -> throw IllegalStateException("Unexpected operation: $operation")
        }.toString()
    }

    private fun isUnary(value: String, prevValue: String?): Boolean {
        val isAllowedPrevValue = prevValue in (listOf("(", null) + operations)
        return value in listOf("+", "-") && isAllowedPrevValue
    }

    private fun isNumber(value: String): Boolean = value.toDoubleOrNull() != null

    private fun isOperation(value: String): Boolean = value in operations

    private fun getOperationPriority(operation: String): Int {
        return when (operation) {
            "u+", "u-" -> 4
            "*", "/" -> 3
            "+", "-" -> 2
            "(" -> 1
            else -> throw IllegalStateException("Unexpected operation: $operation")
        }
    }
}



