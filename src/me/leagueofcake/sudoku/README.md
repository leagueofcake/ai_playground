# Sudoku Solver
[Wikipedia](https://en.wikipedia.org/wiki/Sudoku)

Fairly basic solver for Sudoku puzzles.

A command-line interface (SudokuCLI) is supplied to interact with the Solver, allowing you to input a board and 
solve it.

## Solver Strategy
The solver attempts some basic eliminations and fills in all empty spaces that have definite answers. Once all definite
slots have been filled, the solver attempts to apply various guesses, backtracking when an invalid board state is
discovered. 

## Command-Line Interface (CLI) Commands

The CLI operates on a current puzzle state. Initially, there is no currently loaded puzzle - manually input one using 
`i` (input a row of integers at a time, with `.` to denote empty spaces in the board). 

Once a board has been solved, the solution state will be printed out.

| Command | Description                                |
| ------- | -------------------------------------------|
| i       | Input a Sudoku board from the command line |
| p       | Prints the currently loaded board          |
| solve   | Solve the Sudoku puzzle                    |
| ?       | Displays the command list                  |
| q       | Quits the CLI                              |
