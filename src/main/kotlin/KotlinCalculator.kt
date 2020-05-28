package org.jetbrains

import java.util.*

/*
Create a simple calculator that given a string of operators (), +, -, *, / and numbers separated by spaces
returns the value of that expression

Example: evaluate("1 + 1") => 2
*/
class KotlinCalculator {

    private val operations = listOf("+", "-", "*", "/", "(")

    fun evaluate(str: String): Double {
        val expression = str.split(" ").filter { value -> value.isNotBlank() }
        if (expression.isEmpty()) throw IllegalStateException("Empty input expression")
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
                    val currOp = if (isUnaryInExpression(value, expression.getOrNull(index - 1))) "u$value" else value
                    val operationPriority = getOperationPriority(currOp)
                    while (operationsStack.isNotEmpty() &&
                        ((!isUnary(currOp) && operationPriority <= getOperationPriority(operationsStack.first))
                                || (isUnary(currOp) && operationPriority < getOperationPriority(operationsStack.first)))
                    ) {
                        processOperation(calculations, operationsStack.pop())
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

        if (calculations.size != 1) throw IllegalStateException("Not enough operations to calculate")

        return calculations
    }

    private fun processOperation(calculations: Deque<String>, operation: String) {
        val isUnaryOp = isUnary(operation)
        val realOperation = if (isUnaryOp) operation.last().toString() else operation
        if (!isUnaryOp && calculations.size < 2 || calculations.isEmpty()) {
            throw IllegalStateException("No numbers to calculate with operation $realOperation")
        }
        val firstArg = calculations.pop()
        val secondArg = if (isUnaryOp) "0" else calculations.pop()
        if (operation == "/" && firstArg.toDouble() == 0.0) throw IllegalStateException("Division by zero")
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

    private fun isUnaryInExpression(value: String, prevValue: String?): Boolean {
        val isAllowedPrevValue = prevValue in (listOf(null, *operations.toTypedArray()))
        return value in listOf("+", "-") && isAllowedPrevValue
    }

    private fun isUnary(operation: String) = operation.contains("u")

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



