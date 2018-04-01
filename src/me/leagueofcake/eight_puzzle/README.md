# n-puzzle Solver
[Wikipedia](https://en.wikipedia.org/wiki/15_puzzle)

Solver for the n-puzzle (default 8-puzzle, but configurable using `EightPuzzle.PUZZLE_WIDTH` and 
`EightPuzzle.PUZZLE_HEIGHT`). 

A command-line interface (EightPuzzleCLI) is supplied to interact with the Solver, allowing you to randomly generate
n-puzzles, play with a n-puzzle (with a rudimentary control scheme), automatically solve a given n-puzzle using various
approaches, and more!

## Solver Strategies
Three methods of solving have been implemented: 
* Breadth-First Search (BFS) - guaranteed optimal solution, no heuristics used
* Greedy Search (Manhattan Distance heuristic) - guaranteed solution, no guarantee on optimality
* A* Search (Manhattan Distance heuristic) - guaranteed optimal solution. Tends to be faster than BFS. 

## Command-Line Interface (CLI) Commands

The CLI operates on a current puzzle state. Initially, there is no currently loaded puzzle - either generate one using 
`gen` or manually input one using `i` (input a row of integers at a time, separated by spaces). 

Once a puzzle state has been solved using one of the above approaches, the CLI will print out the number of nodes 
expanded and the number of moves required to solve the puzzle.

### Manipulate Board State
| Command | Description                                                   |
| ------- | ------------------------------------------------------------- |
| gen     | Generate a solvable puzzle and sets the current puzzle to it. |
| i       | Input a puzzle from the command line                          |
| u       | Move the space up (manual puzzle control)                     |
| d       | Move the space down (manual puzzle control)                   |
| l       | Move the space left (manual puzzle control)                   |
| r       | Move the space right (manual puzzle control)                  |

### Solve Puzzle
| Command | Description                                                   |
| ------- | ------------------------------------------------------------- |
| bfs     | Solve the puzzle using BFS                                    |
| greedy  | Solve the puzzle using a greedy approach                      |
| a*      | Solve the puzzle using A*                                     |

### Information/Miscellaneous
| Command | Description                                                   |
| ------- | ------------------------------------------------------------- |
| p       | Prints the currently loaded board                             |
| s       | Prints whether the board is solved                            |
| chk     | Prints whether the current board is solvable                  |
| ?       | Displays the command list                                     |
| q       | Quits the CLI                                                 |
