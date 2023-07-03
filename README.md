## Sudoku Solver
This is a Sudoku solver implemented in Scala using functional programming principles and ZIO for error handling and console interaction. The solver takes a Sudoku problem in JSON format, attempts to solve it recursively, and displays the solution if one exists.

## Requirements
To run the Sudoku solver, you will need the following:

Scala 3.3.0
sbt

## Getting Started
Follow these steps to run the Sudoku solver:

Clone the Git repository: git clone <repository-url>
Change into the project directory: cd sudoku-solver
Build the project: sbt compile
Run the solver: sbt run

## Data Structure
The Sudoku problem and solution are represented using a 9x9 grid of cells, where each cell can contain an optional integer value. The data structure used is type SudokuGrid = List[List[Option[Int]]], which represents the grid as a list of lists.

## File Input
The solver interacts with the user through the ZIO Console. It requests the user to provide the path to a JSON file containing a Sudoku problem. The JSON file should have the following structure:

```json
{
  "grid": [
    [5, 3, null, null, 7, null, null, null, null],
    [6, null, null, 1, 9, 5, null, null, null],
    [null, 9, 8, null, null, null, null, 6, null],
    [8, null, null, null, 6, null, null, null, 3],
    [4, null, null, 8, null, 3, null, null, 1],
    [7, null, null, null, 2, null, null, null, 6],
    [null, 6, null, null, null, null, 2, 8, null],
    [null, null, null, 4, 1, 9, null, null, 5],
    [null, null, null, null, 8, null, null, 7, 9]
  ]
}
```
Make sure the JSON file contains a valid Sudoku problem.

## Error Handling
The Sudoku solver handles various error scenarios using ZIO's error handling capabilities.

## Recursive Approach
The Sudoku solver uses a backtracking recursive approach to solve the puzzle. It starts with an empty grid and recursively tries to fill in each empty cell with a valid value. If a solution is found, it is returned; otherwise, the solver backtracks and tries a different value.


## Additional Notes
The solver uses ZIO for its error handling and console interaction capabilities. ZIO provides a functional and composable way to handle effects and errors.
The solver outputs the provided Sudoku problem and its solution (if one exists) in a nicely formatted grid format using the prettySudokuGrid function.
The solver does not modify the original Sudoku problem grid. It operates on an immutable grid and returns a new grid with the solution, if found.

## Libraries Used
The Sudoku solver uses the following external libraries:

zio - Provides the core functionality for effectful programming, error handling, and console interaction.
zio-json - Provides JSON parsing and serialization capabilities for ZIO.
zio-nio - Offers file reading capabilities for ZIO.
