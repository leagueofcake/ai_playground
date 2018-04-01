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

    /**
     * Constructor for the Solver
     *
     * @param b - the SudokuBoard to be solved
     */
    public Solver (SudokuBoard b) {
        boardToBeSolved = new SudokuBoard(b);
    }

    /**
     * Solves a given Sudoku board using elimination, guesses and backtracking.
     *
     */
    public void solveSudoku() {
        BoardNode currNode = new BoardNode(null, boardToBeSolved);

        Stack<BoardNode> nodeStack = new Stack<>();
        Stack<Guess> guessStack = new Stack<>();

        // Find all definite matches
        currNode.current.findAllMatches();

        // Generate a Guess to test
        Guess currGuess = currNode.current.nextGuess();
        nodeStack.push(currNode);
        guessStack.push(currGuess);

        // System.out.println("GUESSING STAGE");
        while (!currNode.current.isSolved()) {
            // System.out.println("STILL SOLVING!");
            // for (int i = 0; i < 9; i++) {
                // System.out.println(Arrays.toString(currNode.current.board[i]));
            // }

            if (currGuess.possible.empty()) { // No more candidates - backtrack
                // System.out.println(String.format("NO MORE CANDIDATES FOR: (%d, %d)", currGuess.point.col, currGuess.point.row));
                nodeStack.pop();
                guessStack.pop();
                currNode = nodeStack.peek();
                currGuess = guessStack.peek();
            } else { // Try next candidate in guess
                currNode = new BoardNode(currNode, new SudokuBoard(nodeStack.peek().current));
                currGuess = guessStack.peek();
                currNode.current.applyGuess(currGuess.point, currGuess.possible.pop());
                nodeStack.push(currNode);

                int matchesFound = currNode.current.findMatch();
                if (matchesFound == -1) { // Invalid guess, try other guess
                    // System.out.println("BACKTRACKING");
                    nodeStack.pop();
                    currNode = currNode.parent;
                    currGuess = guessStack.peek();
                } else if (matchesFound == 0) { // All possible matches found - make a new guess
                    currGuess = currNode.current.nextGuess();
                    guessStack.push(currGuess);
                } else { // Matches found - keep trying to match
                    while (matchesFound > 0) {
                        matchesFound = boardToBeSolved.findMatch();
                    }
                    if (matchesFound == -1) { // Invalid guess, try other guess
                        // System.out.println("BACKTRACKING");
                        nodeStack.pop();
                        currNode = currNode.parent;
                        currGuess = guessStack.peek();
                    } else { // All posssible matches found - make a new guess
                        currGuess = currNode.current.nextGuess();
                        guessStack.push(currGuess);
                    }
                }
            }
        }

        // Copy board state from b to original board
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (boardToBeSolved.getBoard()[row][col] == '.') {
                    boardToBeSolved.getBoard()[row][col] = currNode.current.getBoard()[row][col];
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
