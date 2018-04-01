package me.leagueofcake.sudoku;

import java.util.Arrays;
import java.util.Stack;

/**
 * Solver class for Sudoku.
 *
 * Solves each board using basic elimination and guesses with backtracking.
 */
class Solver {
    private final SudokuBoard boardToBeSolved;

//    /**
//     * Runner for the solver. Takes in boards as 2d character arrays, with '.' representing an empty space
//     * and '1' - '9' representing their corresponding numbers.
//     *
//     * @param args - Not used.
//     */
//    public static void main(String[] args) {
//        char[][] board = new char[][]{{'.', '.', '9', '7', '4', '8', '.', '.', '.'}, {'7', '.', '.', '.', '.', '.', '.', '.', '.'}, {'.', '2', '.', '1', '.', '9', '.', '.', '.'}, {'.', '.', '7', '.', '.', '.', '2', '4', '.'}, {'.', '6', '4', '.', '1', '.', '5', '9', '.'}, {'.', '9', '8', '.', '.', '.', '3', '.', '.'}, {'.', '.', '.', '8', '.', '3', '.', '2', '.'}, {'.', '.', '.', '.', '.', '.', '.', '.', '6'}, {'.', '.', '.', '2', '7', '5', '9', '.', '.'}};
//        char[][] board2 = new char[][]{{'.', '.', '.', '3', '.', '.', '.', '.', '9'}, {'.', '7', '.', '.', '.', '1', '.', '2', '.'}, {'5', '.', '6', '.', '.', '9', '4', '.', '.'}, {'.', '2', '8', '.', '.', '.', '6', '4', '.'}, {'.', '.', '.', '8', '9', '.', '.', '.', '.'}, {'.', '3', '1', '.', '.', '.', '9', '.', '7'}, {'.', '.', '.', '.', '5', '.', '3', '.', '.'}, {'1', '.', '7', '.', '.', '4', '.', '.', '8'}, {'4', '.', '.', '6', '.', '2', '.', '5', '.'}};
//        char[][] board3 = new char[][]{{'.', '.', '.', '.', '5', '6', '9', '.', '.'}, {'.', '.', '.', '2', '.', '.', '.', '7', '5'}, {'.', '.', '.', '.', '.', '1', '.', '4', '8'}, {'.', '.', '8', '.', '.', '.', '.', '.', '3'}, {'.', '.', '.', '4', '2', '3', '.', '.', '.'}, {'9', '.', '.', '.', '.', '.', '1', '.', '.'}, {'1', '4', '.', '9', '.', '.', '.', '.', '.'}, {'6', '7', '.', '.', '.', '5', '.', '.', '.'}, {'.', '.', '9', '3', '4', '.', '.', '.', '.'}};
//        char[][] board4 = new char[][]{{'5', '.', '.', '3', '.', '.', '.', '7', '.'}, {'3', '4', '.', '7', '.', '9', '.', '.', '.'}, {'.', '.', '.', '.', '.', '.', '1', '.', '.'}, {'1', '5', '.', '.', '.', '.', '7', '.', '.'}, {'.', '9', '.', '.', '6', '.', '.', '5', '.'}, {'.', '.', '2', '.', '.', '.', '.', '8', '4'}, {'.', '.', '5', '.', '.', '.', '.', '.', '.'}, {'.', '.', '.', '9', '.', '8', '.', '1', '6'}, {'.', '7', '.', '.', '.', '3', '.', '.', '9'}};
//        char[][] board5 = new char[][]{{'.', '2', '3', '.', '.', '5', '.', '.', '7'}, {'.', '.', '.', '.', '.', '.', '3', '.', '.'}, {'6', '.', '8', '1', '.', '.', '.', '.', '.'}, {'2', '.', '.', '5', '.', '7', '.', '1', '.'}, {'.', '.', '.', '3', '.', '8', '.', '.', '.'}, {'.', '6', '.', '2', '.', '1', '.', '.', '5'}, {'.', '.', '.', '.', '.', '3', '2', '.', '4'}, {'.', '.', '4', '.', '.', '.', '.', '.', '.'}, {'9', '.', '.', '6', '.', '.', '5', '7', '.'}};
//        char[][] board6 = new char[][]{{'.', '.', '.', '.', '.', '.', '.', '.', '.'}, {'.', '.', '.', '.', '.', '3', '.', '8', '5'}, {'.', '.', '1', '.', '2', '.', '.', '.', '.'}, {'.', '.', '.', '5', '.', '7', '.', '.', '.'}, {'.', '.', '4', '.', '.', '.', '1', '.', '.'}, {'.', '9', '.', '.', '.', '.', '.', '.', '.'}, {'5', '.', '.', '.', '.', '.', '.', '7', '3'}, {'.', '.', '2', '.', '1', '.', '.', '.', '.'}, {'.', '.', '.', '.', '4', '.', '.', '.', '9'}};
//
//        Solver s = new Solver();
//        s.solveSudoku(new SudokuBoard(board));
//        s.solveSudoku(new SudokuBoard(board2));
//        s.solveSudoku(new SudokuBoard(board3));
//        s.solveSudoku(new SudokuBoard(board4));
//        s.solveSudoku(new SudokuBoard(board5));
//        s.solveSudoku(new SudokuBoard(board6));
//    }

    public Solver (SudokuBoard b) {
        boardToBeSolved = new SudokuBoard(b);
    }

    /**
     * Solves a given Sudoku board using elimination, guesses and backtracking.
     *
     */
    public void solveSudoku() {
        Stack<SudokuBoard> boardStack = new Stack<>();
        Stack<Guess> guessStack = new Stack<>();

        // Find all definite matches
        boardToBeSolved.findAllMatches();
        SudokuBoard currBoard = boardToBeSolved;

        // Generate a Guess to test
        Guess currGuess = boardToBeSolved.nextGuess();
        boardStack.push(currBoard);
        guessStack.push(currGuess);

        // System.out.println("GUESSING STAGE");
        while (!currBoard.isSolved()) {
            // System.out.println("STILL SOLVING!");
            // for (int i = 0; i < 9; i++) {
                // System.out.println(Arrays.toString(currBoard.board[i]));
            // }

            if (currGuess.possible.empty()) { // No more candidates - backtrack
                // System.out.println(String.format("NO MORE CANDIDATES FOR: (%d, %d)", currGuess.point.col, currGuess.point.row));
                boardStack.pop();
                guessStack.pop();
                currBoard = boardStack.peek();
                currGuess = guessStack.peek();
            } else { // Try next candidate in guess
                currBoard = new SudokuBoard(boardStack.peek());
                currGuess = guessStack.peek();
                currBoard.applyGuess(currGuess.point, currGuess.possible.pop());
                boardStack.push(currBoard);

                int matchesFound = currBoard.findMatch();
                if (matchesFound == -1) { // Invalid guess, try other guess
                    // System.out.println("BACKTRACKING");
                    boardStack.pop();
                    currBoard = currBoard.getParent();
                    currGuess = guessStack.peek();
                } else if (matchesFound == 0) { // All possible matches found - make a new guess
                    currGuess = currBoard.nextGuess();
                    guessStack.push(currGuess);
                } else { // Matches found - keep trying to match
                    while (matchesFound > 0) {
                        matchesFound = boardToBeSolved.findMatch();
                    }
                    if (matchesFound == -1) { // Invalid guess, try other guess
                        // System.out.println("BACKTRACKING");
                        boardStack.pop();
                        currBoard = currBoard.getParent();
                        currGuess = guessStack.peek();
                    } else { // All posssible matches found - make a new guess
                        currGuess = currBoard.nextGuess();
                        guessStack.push(currGuess);
                    }
                }
            }
        }

        // Copy board state from b to original board
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (boardToBeSolved.getBoard()[row][col] == '.') {
                    boardToBeSolved.getBoard()[row][col] = currBoard.getBoard()[row][col];
                }
            }
        }

        // Print out solved board state
        System.out.println("Solved!");
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(boardToBeSolved.getBoard()[i]));
        }
    }
}
