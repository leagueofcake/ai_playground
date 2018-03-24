package com.company;

import java.util.Arrays;

public class EightPuzzle {
    public static final int PUZZLE_WIDTH = 3;
    public static final int PUZZLE_HEIGHT = 3;

    private int[][] board;
    private Point spacePoint;

    /**
     * Constructs a board state from a given input board.
     * A 0 denotes the empty space.
     *
     * @param inputBoard - 2d int array of size PUZZLE_WIDTH * PUZZLE_HEIGHT
     */
    public EightPuzzle (int [][] inputBoard) {
        board = new int[PUZZLE_HEIGHT][PUZZLE_WIDTH];
        for (int row = 0; row < PUZZLE_HEIGHT; row++) {
            for (int col = 0; col < PUZZLE_WIDTH; col++) {
                board[row][col] = inputBoard[row][col];

                if (board[row][col] == 0) spacePoint = new Point(row, col);
            }
        }
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < PUZZLE_HEIGHT; row++) {
            sb.append(Arrays.toString(board[row]));
            if (row != PUZZLE_HEIGHT - 1) sb.append('\n');
        }
        return sb.toString();
    }

    public boolean isValidBoard () {
        boolean[] used = new boolean[PUZZLE_WIDTH * PUZZLE_HEIGHT];

        for (int row = 0; row < PUZZLE_HEIGHT; row++) {
            for (int col = 0; col < PUZZLE_WIDTH; col++) {
                int num = board[row][col];
                if (num < 0 || num >= PUZZLE_WIDTH * PUZZLE_HEIGHT) return false;
                if (used[num]) return false;
                used[num] = true;
            }
        }
        return true;
    }

    private void swap (Point p1, Point p2) {
        int temp = board[p1.row][p1.col];
        board[p1.row][p1.col] = board[p2.row][p2.col];
        board[p2.row][p2.col] = temp;
    }

    public void moveUp () {
        if (spacePoint.row > 0) {
            swap(spacePoint, new Point(spacePoint.row - 1, spacePoint.col));
            spacePoint.row--;
        }
    }

    public void moveDown () {
        if (spacePoint.row + 1 < PUZZLE_HEIGHT) {
            swap(spacePoint, new Point(spacePoint.row + 1, spacePoint.col));
            spacePoint.row++;
        }
    }

    public void moveLeft () {
        if (spacePoint.col > 0) {
            swap(spacePoint, new Point(spacePoint.row, spacePoint.col - 1));
            spacePoint.col--;
        }
    }

    public void moveRight () {
        if (spacePoint.col + 1 < PUZZLE_WIDTH) {
            swap(spacePoint, new Point(spacePoint.row, spacePoint.col + 1));
            spacePoint.col++;
        }
    }
}
