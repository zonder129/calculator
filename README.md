## Description
A simple calculator that given a string of operators `(), +, -, *, /` and numbers separated by spaces returns the value of that expression
## Test plan 

### Testing approach
Unit-test coverage by following aspects:
* operations validation
* operations priority validation
* exceptions (or boundaries) validation

First two aspects have to be validated with the pairwise method.

### Testing artifacts
CSV files for pairwise testing (located in resources folder):
operation_pairwise.csv

### Test cases

Test cases based on the table of equivalence classes:
1. Operations validation:

First operand | Operator | Second operand
--- | --- | ---
Positive | + | Positive
Negative | - | Negative
Zero | * | Fraction
Fraction | / | Negative fraction
Negative fraction | |