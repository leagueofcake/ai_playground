package me.leagueofcake.eight_puzzle;

import java.util.*;

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

    public EightPuzzle (EightPuzzle orig) {
        board = new int[PUZZLE_HEIGHT][PUZZLE_WIDTH];
        for (int row = 0; row < PUZZLE_HEIGHT; row++) {
            System.arraycopy(orig.board[row], 0, board[row], 0, orig.board.length);
        }
        spacePoint = new Point(orig.spacePoint.row, orig.spacePoint.col);
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

    public boolean canMoveUp () {
        return spacePoint.row > 0;
    }

    public boolean canMoveDown () {
        return spacePoint.row + 1 < PUZZLE_HEIGHT;
    }

    public boolean canMoveLeft () {
        return spacePoint.col > 0;
    }

    public boolean canMoveRight () {
        return spacePoint.col + 1 < PUZZLE_WIDTH;
    }

    public void moveUp () {
        if (canMoveUp()) {
            swap(spacePoint, new Point(spacePoint.row - 1, spacePoint.col));
            spacePoint.row--;
        }
    }

    public void moveDown () {
        if (canMoveDown()) {
            swap(spacePoint, new Point(spacePoint.row + 1, spacePoint.col));
            spacePoint.row++;
        }
    }

    public void moveLeft () {
        if (canMoveLeft()) {
            swap(spacePoint, new Point(spacePoint.row, spacePoint.col - 1));
            spacePoint.col--;
        }
    }

    public void moveRight () {
        if (canMoveRight()) {
            swap(spacePoint, new Point(spacePoint.row, spacePoint.col + 1));
            spacePoint.col++;
        }
    }

    public List<EightPuzzle> generatePossibleMoves () {
        List<EightPuzzle> possibleMoves = new ArrayList<>();

        if (canMoveUp()) {
            EightPuzzle up = new EightPuzzle(this);
            up.moveUp();
            possibleMoves.add(up);
        }

        if (canMoveDown()) {
            EightPuzzle down = new EightPuzzle(this);
            down.moveDown();
            possibleMoves.add(down);
        }

        if (canMoveLeft()) {
            EightPuzzle left = new EightPuzzle(this);
            left.moveLeft();
            possibleMoves.add(left);
        }

        if (canMoveRight()) {
            EightPuzzle right = new EightPuzzle(this);
            right.moveRight();
            possibleMoves.add(right);
        }

        return possibleMoves;
    }

    public boolean isSolved () {
        if (!isValidBoard()) return false;
        int expectedNum = 1;
        for (int row = 0; row < PUZZLE_HEIGHT; row++) {
            for (int col = 0; col < PUZZLE_WIDTH; col++) {
                if (row == PUZZLE_HEIGHT - 1 && col == PUZZLE_WIDTH - 1) return board[row][col] == 0;
                if (board[row][col] != expectedNum) return false;
                expectedNum++;
            }
        }
        return true;
    }

    public int heuristicManhattan () {
        int totalDistance = 0;
        for (int row = 0; row < PUZZLE_HEIGHT; row++) {
            for (int col = 0; col < PUZZLE_WIDTH; col++) {
                int num = board[row][col];
                int targetRow, targetCol;
                if (num == 0) {
                    targetRow = PUZZLE_HEIGHT - 1;
                    targetCol = PUZZLE_WIDTH - 1;
                } else {
                    targetRow = (num - 1) / PUZZLE_HEIGHT;
                    targetCol = (num - 1) % PUZZLE_HEIGHT;
                }
                totalDistance += (Math.abs(targetRow - row) + Math.abs(targetCol - col));
            }
        }

        return totalDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EightPuzzle that = (EightPuzzle) o;
        return Arrays.deepEquals(board, that.board) &&
                Objects.equals(spacePoint, that.spacePoint);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(spacePoint);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
