package me.leagueofcake.eight_puzzle;

import java.util.*;

/**
 * EightPuzzle class representing a sliding n-puzzle (default 8-puzzle).
 *
 * Contains a 2d int array representing the board, with 0 representing the space.
 * Also tracks a Point property to speed up movement and possible move generation.
 *
 * Modify PUZZLE_WIDTH and PUZZLE_HEIGHT to create boards of different sizes. The generators and solver methods
 * should adapt accordingly.
 */
public class EightPuzzle {
    public static final int PUZZLE_WIDTH = 3;
    public static final int PUZZLE_HEIGHT = 3;

    private final int[][] board;
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

    /**
     * Copy constructor.
     *
     * @param orig - Original EightPuzzle
     */
    public EightPuzzle (EightPuzzle orig) {
        board = new int[PUZZLE_HEIGHT][PUZZLE_WIDTH];
        for (int row = 0; row < PUZZLE_HEIGHT; row++) {
            System.arraycopy(orig.board[row], 0, board[row], 0, orig.board.length);
        }
        spacePoint = new Point(orig.spacePoint.row, orig.spacePoint.col);
    }

    /**
     * Checks whether the current board is valid.
     * A board is valid if:
     *   - There are no duplicate numbers
     *   - Each number from 0 - (PUZZLE_WIDTH * PUZZLE_HEIGHT - 1) is contained in the board
     *
     * @return Whether the board is valid
     */
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

    /**
     * Helper function to swap the contents of the board at Points p1 and p2.
     *
     * @param p1 First point to have contents swapped
     * @param p2 Second point to have contents swapped
     */
    private void swap (Point p1, Point p2) {
        int temp = board[p1.row][p1.col];
        board[p1.row][p1.col] = board[p2.row][p2.col];
        board[p2.row][p2.col] = temp;
    }

    /**
     * @return Whether the space (0) can move up.
     */
    public boolean canMoveUp () {
        return spacePoint.row > 0;
    }

    /**
     * @return Whether the space (0) can move down.
     */
    public boolean canMoveDown () {
        return spacePoint.row + 1 < PUZZLE_HEIGHT;
    }

    /**
     * @return Whether the space (0) can move left.
     */
    public boolean canMoveLeft () {
        return spacePoint.col > 0;
    }

    /**
     * @return Whether the space (0) can move right
     */
    public boolean canMoveRight () {
        return spacePoint.col + 1 < PUZZLE_WIDTH;
    }

    /**
     * Moves the space (0) up if possible, otherwise does nothing.
     */
    public void moveUp () {
        if (canMoveUp()) {
            swap(spacePoint, new Point(spacePoint.row - 1, spacePoint.col));
            spacePoint.row--;
        }
    }

    /**
     * Moves the space (0) down if possible, otherwise does nothing.
     */
    public void moveDown () {
        if (canMoveDown()) {
            swap(spacePoint, new Point(spacePoint.row + 1, spacePoint.col));
            spacePoint.row++;
        }
    }

    /**
     * Moves the space (0) left if possible, otherwise does nothing.
     */
    public void moveLeft () {
        if (canMoveLeft()) {
            swap(spacePoint, new Point(spacePoint.row, spacePoint.col - 1));
            spacePoint.col--;
        }
    }

    /**
     * Moves the space (0) right if possible, otherwise does nothing.
     */
    public void moveRight () {
        if (canMoveRight()) {
            swap(spacePoint, new Point(spacePoint.row, spacePoint.col + 1));
            spacePoint.col++;
        }
    }

    /**
     * Generates the possible EightPuzzle states that can be reached from the current state.
     *
     * @return A List of possible EightPuzzle states
     */
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

    /**
     * Checks whether the current board is solved, by checking each number in the board corresponds to its
     * expected number.
     *
     * An invalid board counts as unsolved.
     *
     * @return Whether the board is solved.
     */
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

    /**
     * Heuristic function based on the Manhattan distance to the goal state.
     *
     * @return Total Manhattan distance to the goal state.
     */
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

    /**
     * Checks whether a board is solvable by counting the number of inversions on the board.
     * A board is solvable if the number of inversions is even.
     *
     * @return Whether the board is solvable.
     */
    public boolean isSolvable() {
        int inversions = 0;
        int[] arr = new int[EightPuzzle.PUZZLE_WIDTH * EightPuzzle.PUZZLE_HEIGHT];
        int curr = 0;
        for (int row = 0; row < EightPuzzle.PUZZLE_HEIGHT; row++) {
            for (int col = 0; col < EightPuzzle.PUZZLE_WIDTH; col++) {
                arr[curr++] = board[row][col];
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] != 0 && arr[j] != 0 && arr[j] < arr[i]) {
                    inversions++;
                }
            }
        }

        return inversions % 2 == 0;
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
