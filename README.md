## Description
A simple calculator that given a string of operators `(), +, -, *, /` and numbers separated by spaces returns the value of that expression
## Test plan 

### Testing approach
Unit-test coverage by following aspects:
* operations validation
* operations priority validation
* exceptions (or boundaries) validation

First aspect have to be validated with the pairwise method.

### Testing artifacts
CSV files for pairwise testing (located in resources folder):
operation_pairwise.csv

### Test cases

Test cases based on the table of equivalence classes:

1\. Operations validation:

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

3\. Test cases based on exceptional / boundary use cases:

№ | Description 
--- | --- 
1 | Very large positive numbers (more than Double type size) results into infinity
2 | Very large negative numbers (more than Double type size) results into -infinity
3 | Very small positive fraction results into zero 
4 | Not a numbers can't be processed
5 | Not an operation can't be processed
6 | Comma as fraction symbol can't be processed
7 | Double fraction dot symbol can't be processed
8 | Common fractions can't be processed
9 | Numbers with leading zeros are treated as usual numbers
10 | Expression with missing opening bracket can't be processed
11 | Expression with missing closing bracket can't be processed
12 | Expression with extra spaces is calculated properly
13 | Division by zero returns infinity
14 | Division fraction by zero returns infinity
15 | Division zero by zero returns NaN
16 | Unary minus before number is calculated properly
17 | Unary minus before bracket is calculated properly
18 | Unary plus before number is calculated properly
19 | Unary plus before bracket is calculated properly

