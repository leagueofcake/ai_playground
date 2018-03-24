package com.company;

import java.util.Objects;

public class PuzzleNode {
    public EightPuzzle current;
    public PuzzleNode parent;
    public int depth;

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
