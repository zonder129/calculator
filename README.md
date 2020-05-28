## Description
A simple calculator that given a string of operators `(), +, -, *, /` and numbers separated by spaces returns the value of that expression
## Test plan 

### Testing approach
Unit-test coverage by following classes of equivalence:
* binary operations validation
* operations priority
* parentheses validation
* unary operations validation
* unary signs aggregations validation
* fraction calculations validation
* validation of calculation boundaries
* exceptional input validation
* exceptional calculations validation

First two aspects have to be validated with the pairwise method.

### Testing artifacts
CSV files for pairwise testing (located in resources folder):
`operation_pairwise.csv`

`priorities_pariwise.csv`

### Test cases

Test cases based on the table of equivalence classes:

1\. Binary operations validation:

First operand | Operator | Second operand
--- | --- | ---
Positive | + | Positive
Negative | - | Negative
Zero | * | Fraction
Fraction | / | Negative fraction
Negative fraction | |

2\. Test cases based on the table of equivalence classes for priorities of operations

First operand | First operator | Second operand | Second operator | Third operand | Brackets (btw op 1 and 2)
--- | --- | --- | --- | --- | ---
Number | + | Number | * | Number | true
| - | | / | | | false

3\. All tests for other equivalence classes were written as separate unit tests:

№ | Description 
--- | --- 
- | Calculation Boundaries
1 | Very large positive numbers (more than Double type size) results into infinity
2 | Very large negative numbers (more than Double type size) results into -infinity
- | Unary Operations
3 | Unary minus before number is calculated properly
4 | Unary minus before bracket is calculated properly
5 | Unary plus before number is calculated properly
6 | Unary plus before bracket is calculated properly
- | Unary Signs Aggregations
7 | Many even minuses calculating properly
8 | Many odd minuses calculating properly
9 | Many pluses calculating properly
- | Fraction Calculations
10 | Fraction without zero calculating properly
11 | More than one fraction dot symbol can't be processed
12 | Comma as fraction symbol can't be processed
13 | Common fractions can't be processed
14 | Very small positive fraction results into zero
- | Priorities And Parentheses
15 | Series of operators with different priorities calculating properly
16 | Expression with missing opening bracket can't be processed
17 | Expression with missing closing bracket can't be processed
18 | Nested brackets calculating properly
19 | Brackets around number calculating properly
- | Exceptional input
20 | Not a numbers can't be processed
21 | Not an operation can't be processed
22 | Extra operation without associated operator can't be processed
23 | Operands without operation can't be processed
24 | Empty input can't be processed
25 | Spaces input can't be processed
- | Exceptional Calculations
26 | Numbers with leading zeros are treated as usual numbers
27 | Expression with extra spaces is calculated properly
28 | Division by zero can't be processed
29 | Division fraction by zero can't be processed
30 | Division by zero expression can't be processed
