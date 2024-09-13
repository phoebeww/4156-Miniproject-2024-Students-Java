# 4156 Individual Assignment 1

## Test Suite Notes
- A new data test file `dbtest.txt` has been created for the `DatabaseUnitTests` to test some specific functions. This 
file is automatically cleared once all the tests have been executed, ensuring no residual data is left behind. 
- The `RouteUnitTests` make use of the provided `data.txt` file for testing purposes. These tests may modify the contents 
of `data.txt` during execution. Please run the provided setup command if reset data is needed.
## PMD Command Instructions
If PMD is not installed already, please refer to the official [PMD Website](https://pmd.github.io/).

To run the PMD static bug finder, navigate to the`4156-Miniproject-2024-Students-Java/IndividualProject` 
directory and execute the following command:

`pmd check -d ./src -R rulesets/java/quickstart.xml -f text`

To redirect the output to a file, use the following command:

`pmd check -d ./src -R rulesets/java/quickstart.xml -f text -r pmd_output.txt`

This will generate a `pmd_output.txt` file containing the analysis results. If `pmd` command is not working, 
run `alias pmd="$HOME/pmd-bin-7.5.0/bin/pmd"`.
