package me.leagueofcake.sudoku;

import java.util.Objects;

public class Point {
    public byte row, col;

    /**
     * Constructs a point from a given row and col value.
     * @param row Row number
     * @param col Column number
     */
    public Point (byte row, byte col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return row == point.row &&
                col == point.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
