package org.jetbrains

import kotlin.IllegalStateException

/*
Create a simple calculator that given a string of operators (), +, -, *, / and numbers separated by spaces
returns the value of that expression

Example: evaluate("1 + 1") => 2
*/
fun evaluate(str: String): Double {
    val values = str.split(" ")
    if (values.size < 3) {
        throw IllegalStateException("Less then 3 operations or operand in expression")
    }
    return when(values[1]){
        "*" -> values[0].toDouble() * values[2].toDouble()
        "/" -> values[0].toDouble() / values[2].toDouble()
        "-" -> values[0].toDouble() - values[2].toDouble()
        "+" -> values[0].toDouble() + values[2].toDouble()
        else -> throw IllegalStateException("Operation is not expected: ${values[1]}")
    }
}

// ( 1 + 2 ) * 3
