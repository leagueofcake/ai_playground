package me.leagueofcake.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Board class representing a 9x9 Sudoku board.
 *
 * Stores a 2d char array representing the board, with '.' representing an empty space
 * and '1' - '9' representing their corresponding numbers.
 *
 * Also tracks:
 * -toFillIn - a Set of Points that are currently empty ('.') - saves us having to iterate through the board each time
 *   we want to find all empty slots
 * -rows - A List of Sets (each of which contains the numbers for each row) to help with lookups
 * -cols - A List of Sets (each of which contains the numbers for each column) to help with lookups
 * -boxes - A List of Sets (each of which contains the numbers for each 3x3 box) to help with lookups
 */
public class Board {
    private static int BOARD_SIZE = 9;

    private Board parent;
    private char[][] board;
    private Set<Point> toFillIn;
    private List<Set<Byte>> rows;
    private List<Set<Byte>> cols;
    private List<Set<Byte>> boxes;

    /**
     * Copy constructor
     * @param b - Board object to be copied
     */
    public Board(Board b) {
        parent = b;
        rows = new ArrayList<>();
        cols = new ArrayList<>();
        boxes = new ArrayList<>();
        toFillIn = new HashSet<>(b.toFillIn);

        // Copy 2d board array
        board = new char[BOARD_SIZE][BOARD_SIZE];
        for (byte row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(b.board[row], 0, board[row], 0, b.board[row].length);
        }

        // Copy rows, cols and boxes from b
        for (int i = 0; i < BOARD_SIZE; i++) {
            rows.add(new HashSet<>(b.rows.get(i)));
            cols.add(new HashSet<>(b.cols.get(i)));
            boxes.add(new HashSet<>(b.boxes.get(i)));
        }
    }

    /**
     * Constructor for a Board from a 2d char array, with '.' representing an empty space
     *
     * @param board 2d character array representing the board
     */
    public Board(char[][] board) {
        parent = null;
        rows = new ArrayList<>();
        cols = new ArrayList<>();
        boxes = new ArrayList<>();
        toFillIn = new HashSet<>();

        // Copy 2d board array
        this.board = new char[BOARD_SIZE][BOARD_SIZE];
        for (byte row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(board[row], 0, this.board[row], 0, board[row].length);
        }

        // Initialise individual HashSets for each row, column and box
        for (int i = 0; i < BOARD_SIZE; i++) {
            rows.add(new HashSet<>());
            cols.add(new HashSet<>());
            boxes.add(new HashSet<>());
        }

        // Fill in rows, cols and boxes with numbers from each position, or adds Point to toFillIn if empty
        for (byte row = 0; row < BOARD_SIZE; row++) {
            for (byte col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] != '.') {
                    byte box = boxNumber(row, col);
                    byte b = (byte) (board[row][col] - '0');
                    rows.get(row).add(b);
                    cols.get(col).add(b);
                    boxes.get(box).add(b);
                } else {
                    toFillIn.add(new Point(row, col));
                }
            }
        }
    }

    /**
     * Finds and returns the possible numbers for a given position. Numbers are excluded if they appear in the same
     * row, column or box as the requested point.
     *
     * @param row - The row number for the given position
     * @param col - The column number for the given position
     * @param box - The box number for the given position
     * @return A set of Bytes representing the possible numbers for the position.
     */
    private Set<Byte> findPossible(byte row, byte col, byte box) {
        Set<Byte> possible = new HashSet<>();

        for (byte i = 1; i <= BOARD_SIZE; i++) {
            boolean rowContains = rows.get(row).contains(i);
            boolean colContains = cols.get(col).contains(i);
            boolean boxContains = boxes.get(box).contains(i);
            if (!(rowContains || colContains || boxContains)) possible.add(i);
        }
        return possible;
    }

    /**
     * Given a Set of candidate numbers for an empty space, attempts to check the other two rows and columns for
     * the box the space resides in. If the candidate resides in the other two rows and other two columns, then it must
     * fit in the row and column for the space.
     *
     * E.g. (. is the candidate space, _ represents any number).
     * __. ___ ___
     * ___ _7_ ___
     * ___ ___ 7__
     *
     * ___ ___ ___
     * 7__ ___ ___
     * ___ ___ ___
     *
     * ___ ___ ___
     * _7_ ___ ___
     * ___ ___ ___
     *
     * We have that 7 is found in the other two rows (row 2 and 3) and columns (columns 1 and 2) for the given
     * box (top-left) - hence the only place 7 can be placed in that box is at our empty space (row 1, column 3).
     *
     * @param possible - A Set of Bytes representing possible numbers for the given space. Generated by findPossible().
     * @param row - The row for the given space.
     * @param col - The column for the given space.
     * @return The only possible candidate number for the space (if found), or -1 on failure.
     */
    private byte excluder(Set<Byte> possible, byte row, byte col) {
        for (Byte candidate : possible) {
            byte otherRow1 = -1, otherRow2 = -1;
            byte otherCol1 = -1, otherCol2 = -1;

            if (row % 3 == 0) { // 2 below
                otherRow1 = (byte) (row + 1);
                otherRow2 = (byte) (row + 2);
            } else if (row % 3 == 1) { // 1 above 1 below
                otherRow1 = (byte) (row - 1);
                otherRow2 = (byte) (row + 1);
            } else if (row % 3 == 2) { // 2 above
                otherRow1 = (byte) (row - 2);
                otherRow2 = (byte) (row - 1);
            }

            if (!rows.get(otherRow1).contains(candidate)) continue;
            if (!rows.get(otherRow2).contains(candidate)) continue;

            if (col % 3 == 0) { // 2 right
                otherCol1 = (byte) (col + 1);
                otherCol2 = (byte) (col + 2);
            } else if (col % 3 == 1) { // 1 left 1 right
                otherCol1 = (byte) (col - 1);
                otherCol2 = (byte) (col + 1);
            } else if (col % 3 == 2) { // 2 left
                otherCol1 = (byte) (col - 2);
                otherCol2 = (byte) (col - 1);
            }

            if (!cols.get(otherCol1).contains(candidate)) continue;
            if (!cols.get(otherCol2).contains(candidate)) continue;

            // Candidate number is found in other two rows and other two columns - return!
            return candidate;
        }
        return -1; // Candidate not found via exclusion
    }

    /**
     * Attempts to fill in empty spaces in toFillIn that it is certain of (i.e. without guessing).
     *
     * @return The number of matches made (if validly filled in), or -1 if we have reached an invalid board state.
     */
    public int findMatch() {
        int matchesMade = 0;
        Set<Point> used = new HashSet<>();

        for (Point p : toFillIn) {
            byte row = p.row, col = p.col, box = boxNumber(row, col);
            Set<Byte> possible = findPossible(row, col, box);

            if (possible.size() == 0) { // Invalid solution, need to backtrack
                return -1;
            }

            // If only one possible candidate, use it, otherwise try use the excluder
            byte found = (possible.size() == 1) ? possible.iterator().next() : excluder(possible, row, col);

            // Found a match - add to rows, cols and boxes
            if (found != -1) {
                board[row][col] = (char) (found + '0');
                rows.get(row).add(found);
                cols.get(col).add(found);
                boxes.get(box).add(found);
                matchesMade++;
                used.add(p);
                // System.out.println("CANDIDATE FOUND FOR " + col + ", " + row + ": " + found);
            }
        }

        // Clear filled in Points from toFillIn
        toFillIn.removeAll(used);
        return matchesMade;
    }

    /**
     * Helper function applying the formula to find the (3x3) box number for a given row and column.
     * @param row - The row for the point
     * @param col - The column for the point
     * @return The number corresponding to the 3x3 box in the board
     */
    private byte boxNumber (byte row, byte col) {
        return (byte) (3 * (row / 3) + (col / 3));
    }

    /**
     * Attempts to find all definite matches. Stops when no more definite matches are found
     * (i.e. needs to start guessing)
     */
    public void findAllMatches () {
        int matchesMade = findMatch();
        while (matchesMade > 0) {
            matchesMade = findMatch();
        }
    }

    /**
     * Generate a Guess with a Set of possible numbers for the given point.
     *
     * @return Guess object containing the next guess
     */
    public Guess nextGuess() {
        Point guessPoint = null;
        Set<Byte> guessPossible = null;
        for (Point p : toFillIn) {
            byte row = p.row, col = p.col, box = boxNumber(row, col);
            Set<Byte> possible = findPossible(row, col, box);
            if (guessPossible == null || possible.size() < guessPossible.size()) {
                guessPoint = p;
                guessPossible = possible;
            }
        }
        if (guessPoint == null) return null;
        // System.out.println(String.format("GUESS (%d, %d) IS: %s", guessPoint.col, guessPoint.row, guessPossible));
        return new Guess(guessPoint, guessPossible);
    }

    /**
     * Given a Point and a candidate number for the point, adds it to rows, cols and boxes.
     *
     * @param p - Point for the space to be filled
     * @param candidate - Candidate number to be inserted into the space
     */
    public void applyGuess(Point p, byte candidate) {
        toFillIn.remove(p);
        // System.out.println("    TRYING GUESS: " + candidate + " at " + "(" + p.col + ", " + p.row + ")");
        byte row = p.row, col = p.col, box = boxNumber(row, col);
        this.board[row][col] = (char) ('0' + candidate);
        rows.get(row).add(candidate);
        cols.get(col).add(candidate);
        boxes.get(box).add(candidate);
    }

    /**
     * Checks whether the board is solved by checking that each row contains BOARD_SIZE (9) elements.
     *
     * @return Whether the board is solved
     */
    public boolean isSolved() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (rows.get(i).size() != BOARD_SIZE) return false;
        }
        return true;
    }

    /**
     * @return The parent Board for the given Board
     */
    public Board getParent () {
        return parent;
    }

    /**
     * @return The 2d char array representing the board
     */
    public char[][] getBoard () {
        return board;
    }
}
