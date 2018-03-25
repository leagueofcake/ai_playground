package me.leagueofcake.eight_puzzle;

import java.util.Objects;

/**
 * PuzzleNode wrapper class.
 *
 * Tracks:
 * - the current board state
 * - a parent node (to help with generating the solution)
 * - the depth of the current node
 */
public class PuzzleNode {
    public EightPuzzle current;
    public PuzzleNode parent;
    public int depth;

    /**
     * Constructor for PuzzleNode.
     *
     * Current depth is set to parent's depth + 1, or 0 if parent is null (curr is root node).
     *
     * @param parent - the parent node
     * @param current - the current board state
     */
    public PuzzleNode (PuzzleNode parent, EightPuzzle current) {
        this.parent = parent;
        this.current = current;
        if (parent == null) {
            this.depth = 0;
        } else {
            this.depth = parent.depth + 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleNode that = (PuzzleNode) o;
        return Objects.equals(current, that.current) &&
                Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {

        return Objects.hash(current, parent);
    }
}
