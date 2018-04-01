package me.leagueofcake.sudoku;

import java.util.Set;
import java.util.Stack;

/**
 * Guess class - stores a point (row, col) and a Stack of possible numbers (1-9) for the point.
 */
public class Guess {
    public Point point;
    public Stack<Byte> possible;

    /**
     * Constructs a Guess from a given point and a Set of possible numbers for the given point.
     * @param point - Point object (row, col)
     * @param possible - A set of Bytes representing the possible numbers for the point
     */
    public Guess (Point point, Set<Byte> possible) {
        this.point = point;
        this.possible = new Stack<>();
        for (Byte b: possible) {
            this.possible.push(b);
        }
    }
}
