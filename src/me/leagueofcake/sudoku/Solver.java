package me.leagueofcake.sudoku;

import java.util.Arrays;
import java.util.Stack;

class Solver {
    public static void main(String[] args) {
        char[][] board = new char[][]{{'.', '.', '9', '7', '4', '8', '.', '.', '.'}, {'7', '.', '.', '.', '.', '.', '.', '.', '.'}, {'.', '2', '.', '1', '.', '9', '.', '.', '.'}, {'.', '.', '7', '.', '.', '.', '2', '4', '.'}, {'.', '6', '4', '.', '1', '.', '5', '9', '.'}, {'.', '9', '8', '.', '.', '.', '3', '.', '.'}, {'.', '.', '.', '8', '.', '3', '.', '2', '.'}, {'.', '.', '.', '.', '.', '.', '.', '.', '6'}, {'.', '.', '.', '2', '7', '5', '9', '.', '.'}};
        char[][] board2 = new char[][]{{'.', '.', '.', '3', '.', '.', '.', '.', '9'}, {'.', '7', '.', '.', '.', '1', '.', '2', '.'}, {'5', '.', '6', '.', '.', '9', '4', '.', '.'}, {'.', '2', '8', '.', '.', '.', '6', '4', '.'}, {'.', '.', '.', '8', '9', '.', '.', '.', '.'}, {'.', '3', '1', '.', '.', '.', '9', '.', '7'}, {'.', '.', '.', '.', '5', '.', '3', '.', '.'}, {'1', '.', '7', '.', '.', '4', '.', '.', '8'}, {'4', '.', '.', '6', '.', '2', '.', '5', '.'}};
        char[][] board3 = new char[][]{{'.', '.', '.', '.', '5', '6', '9', '.', '.'}, {'.', '.', '.', '2', '.', '.', '.', '7', '5'}, {'.', '.', '.', '.', '.', '1', '.', '4', '8'}, {'.', '.', '8', '.', '.', '.', '.', '.', '3'}, {'.', '.', '.', '4', '2', '3', '.', '.', '.'}, {'9', '.', '.', '.', '.', '.', '1', '.', '.'}, {'1', '4', '.', '9', '.', '.', '.', '.', '.'}, {'6', '7', '.', '.', '.', '5', '.', '.', '.'}, {'.', '.', '9', '3', '4', '.', '.', '.', '.'}};
        char[][] board4 = new char[][]{{'5', '.', '.', '3', '.', '.', '.', '7', '.'}, {'3', '4', '.', '7', '.', '9', '.', '.', '.'}, {'.', '.', '.', '.', '.', '.', '1', '.', '.'}, {'1', '5', '.', '.', '.', '.', '7', '.', '.'}, {'.', '9', '.', '.', '6', '.', '.', '5', '.'}, {'.', '.', '2', '.', '.', '.', '.', '8', '4'}, {'.', '.', '5', '.', '.', '.', '.', '.', '.'}, {'.', '.', '.', '9', '.', '8', '.', '1', '6'}, {'.', '7', '.', '.', '.', '3', '.', '.', '9'}};
        char[][] board5 = new char[][]{{'.', '2', '3', '.', '.', '5', '.', '.', '7'}, {'.', '.', '.', '.', '.', '.', '3', '.', '.'}, {'6', '.', '8', '1', '.', '.', '.', '.', '.'}, {'2', '.', '.', '5', '.', '7', '.', '1', '.'}, {'.', '.', '.', '3', '.', '8', '.', '.', '.'}, {'.', '6', '.', '2', '.', '1', '.', '.', '5'}, {'.', '.', '.', '.', '.', '3', '2', '.', '4'}, {'.', '.', '4', '.', '.', '.', '.', '.', '.'}, {'9', '.', '.', '6', '.', '.', '5', '7', '.'}};
        char[][] board6 = new char[][]{{'.', '.', '.', '.', '.', '.', '.', '.', '.'}, {'.', '.', '.', '.', '.', '3', '.', '8', '5'}, {'.', '.', '1', '.', '2', '.', '.', '.', '.'}, {'.', '.', '.', '5', '.', '7', '.', '.', '.'}, {'.', '.', '4', '.', '.', '.', '1', '.', '.'}, {'.', '9', '.', '.', '.', '.', '.', '.', '.'}, {'5', '.', '.', '.', '.', '.', '.', '7', '3'}, {'.', '.', '2', '.', '1', '.', '.', '.', '.'}, {'.', '.', '.', '.', '4', '.', '.', '.', '9'}};

        Solver s = new Solver();
        s.solveSudoku(board);
        s.solveSudoku(board2);
        s.solveSudoku(board3);
        s.solveSudoku(board4);
        s.solveSudoku(board5);
        s.solveSudoku(board6);
    }

    private void solveSudoku(char[][] board) {
        Board b = new Board(board);
        Stack<Board> boardStack = new Stack<>();
        Stack<Guess> guessStack = new Stack<>();

        b.findAllMatches();
        Board currBoard = b;
        Guess currGuess = b.nextGuess();
        boardStack.push(currBoard);
        guessStack.push(currGuess);

        System.out.println("GUESSING STAGE");
        while (!currBoard.isSolved()) {
            // System.out.println("STILL SOLVING!");
            for (int i = 0; i < 9; i++) {
                // System.out.println(Arrays.toString(currBoard.board[i]));
            }

            if (currGuess.possible.empty()) { // No more candidates - backtrack
                // System.out.println(String.format("NO MORE CANDIDATES FOR: (%d, %d)", currGuess.point.col, currGuess.point.row));
                boardStack.pop();
                guessStack.pop();
                currBoard = boardStack.peek();
                currGuess = guessStack.peek();
            } else { // Try next candidate in guess
                currBoard = new Board(boardStack.peek());
                currGuess = guessStack.peek();
                currBoard.applyGuess(currGuess.point, currGuess.possible.pop());
                boardStack.push(currBoard);

                int matchesFound = currBoard.findMatch();
                if (matchesFound == -1) { // Invalid guess, try other guess
                    System.out.println("BACKTRACKING");
                    boardStack.pop();
                    currBoard = currBoard.getParent();
                    currGuess = guessStack.peek();
                } else if (matchesFound == 0) { // All possible matches found - make a new guess
                    currGuess = currBoard.nextGuess();
                    guessStack.push(currGuess);
                } else { // Matches found - keep trying to match
                    while (matchesFound > 0) {
                        matchesFound = b.findMatch();
                    }
                    if (matchesFound == -1) { // Invalid guess, try other guess
                        System.out.println("BACKTRACKING");
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
                if (board[row][col] == '.') {
                    board[row][col] = currBoard.getBoard()[row][col];
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}
