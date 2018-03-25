package me.leagueofcake.sudoku;

import java.util.Set;
import java.util.Stack;

/**
 * Guess class - stores a point (row, col) and a Stack of possible numbers (1-9) for the point.
 */
public class Guess {
    public Point point;
    public Stack<Byte> possible;

    public Guess (Point point, Set<Byte> possible) {
        this.point = point;
        this.possible = new Stack<>();
        for (Byte b: possible) {
            this.possible.push(b);
        }
    }
}
