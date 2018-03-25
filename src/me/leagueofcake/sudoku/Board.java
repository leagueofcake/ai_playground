package me.leagueofcake.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private static int BOARD_SIZE = 9;

    private Board parent;
    private char[][] board;
    private Set<Point> toFillIn;
    private List<Set<Byte>> rows;
    private List<Set<Byte>> cols;
    private List<Set<Byte>> boxes;

    Board(Board b) {
        parent = b;
        rows = new ArrayList<>();
        cols = new ArrayList<>();
        boxes = new ArrayList<>();
        toFillIn = new HashSet<>(b.toFillIn);

        board = new char[BOARD_SIZE][BOARD_SIZE];
        for (byte row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(b.board[row], 0, board[row], 0, b.board[row].length);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            rows.add(new HashSet<>(b.rows.get(i)));
            cols.add(new HashSet<>(b.cols.get(i)));
            boxes.add(new HashSet<>(b.boxes.get(i)));
        }
    }

    Board(char[][] board) {
        parent = null;
        rows = new ArrayList<>();
        cols = new ArrayList<>();
        boxes = new ArrayList<>();
        toFillIn = new HashSet<>();

        this.board = new char[BOARD_SIZE][BOARD_SIZE];
        for (byte row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(board[row], 0, this.board[row], 0, board[row].length);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            rows.add(new HashSet<>());
            cols.add(new HashSet<>());
            boxes.add(new HashSet<>());
        }

        for (byte row = 0; row < BOARD_SIZE; row++) {
            for (byte col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] != '.') {
                    int box = 3 * (row / 3) + (col / 3);
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

    private Set<Byte> findPossible(byte row, byte col, byte box) {
        Set<Byte> possible = new HashSet<>();

        for (byte i = 1; i <= BOARD_SIZE; i++) {
            if (rows.get(row).contains(i) || cols.get(col).contains(i) || boxes.get(box).contains(i)) continue;
            else possible.add(i);
        }
        return possible;
    }

    private byte excluder(Set<Byte> possible, byte row, byte col) {
        for (Byte candidate : possible) {
            if (row % 3 == 0) { // 2 below
                if (!rows.get(row + 1).contains(candidate)) continue;
                if (!rows.get(row + 2).contains(candidate)) continue;
            } else if (row % 3 == 1) { // 1 above 1 below    
                if (!rows.get(row - 1).contains(candidate)) continue;
                if (!rows.get(row + 1).contains(candidate)) continue;
            } else if (row % 3 == 2) { // 2 above
                if (!rows.get(row - 2).contains(candidate)) continue;
                if (!rows.get(row - 1).contains(candidate)) continue;
            }

            if (col % 3 == 0) { // 2 right
                if (!cols.get(col + 1).contains(candidate)) continue;
                if (!cols.get(col + 2).contains(candidate)) continue;
            } else if (col % 3 == 1) { // 1 left 1 right    
                if (!cols.get(col - 1).contains(candidate)) continue;
                if (!cols.get(col + 1).contains(candidate)) continue;
            } else if (col % 3 == 2) { // 2 left
                if (!cols.get(col - 2).contains(candidate)) continue;
                if (!cols.get(col - 1).contains(candidate)) continue;
            }

            return candidate;
        }
        return -1;
    }

    public int findMatch() {
        int matchesMade = 0;
        Set<Point> used = new HashSet<>();

        for (Point p : toFillIn) {
            byte row = p.row, col = p.col, box = (byte) (3 * (row / 3) + (col / 3));
            Set<Byte> possible = findPossible(row, col, box);

            if (possible.size() == 0) { // Invalid solution, need to backtrack
                return -1; // Invalid solution, need to backtrack
            }

            byte found = (possible.size() == 1) ? possible.iterator().next() : excluder(possible, row, col);

            if (found != -1) {
                board[row][col] = (char) (found + '0');
                rows.get(row).add(found);
                cols.get(col).add(found);
                boxes.get(box).add(found);
                matchesMade++;
                used.add(p);
                System.out.println("CANDIDATE FOUND FOR " + col + ", " + row + ": " + found);
            }
        }

        toFillIn.removeAll(used);
        return matchesMade;
    }

    public void findAllMatches () {
        // Find all definite matches
        int matchesMade = findMatch();
        while (matchesMade > 0) {
            matchesMade = findMatch();
        }
    }

    public Guess nextGuess() {
        Point guessPoint = null;
        Set<Byte> guessPossible = null;
        for (Point p : toFillIn) {
            byte row = p.row, col = p.col, box = (byte) (3 * (row / 3) + (col / 3));
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

    public void applyGuess(Point p, byte candidate) {
        toFillIn.remove(p);
        // System.out.println("    TRYING GUESS: " + candidate + " at " + "(" + p.col + ", " + p.row + ")");
        byte row = p.row, col = p.col, box = (byte) (3 * (row / 3) + (col / 3));
        this.board[row][col] = (char) ('0' + candidate);
        rows.get(row).add(candidate);
        cols.get(col).add(candidate);
        boxes.get(box).add(candidate);
    }

    public boolean isSolved() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (rows.get(i).size() != BOARD_SIZE) return false;
        }
        return true;
    }

    public Board getParent () {
        return parent;
    }

    public char[][] getBoard () {
        return board;
    }
}
