package me.leagueofcake.sudoku;

import java.util.Objects;

/**
 * BoardNode wrapper class.
 *
 * Tracks:
 * - the current board state
 * - a parent node (to help with generating the solution)
 */
public class BoardNode {
    public final SudokuBoard current;
    public final BoardNode parent;

    /**
     * Constructor for BoardNode.
     *
     * @param parent - the parent node
     * @param current - the current board state
     */
    public BoardNode(BoardNode parent, SudokuBoard current) {
        this.parent = parent;
        this.current = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardNode that = (BoardNode) o;
        return Objects.equals(current, that.current) &&
                Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {

        return Objects.hash(current, parent);
    }
}
