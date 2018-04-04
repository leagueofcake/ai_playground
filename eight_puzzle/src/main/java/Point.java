package me.leagueofcake.eight_puzzle;

import java.util.Objects;

public class Point {
    public int row, col;

    /**
     * Constructs a point from a given row and col value.
     * @param row Row number
     * @param col Column number
     */
    public Point (int row, int col) {
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
