package com.company;

import java.util.Objects;

public class PuzzleNode {
    public EightPuzzle current;
    public PuzzleNode parent;

    public PuzzleNode (PuzzleNode parent, EightPuzzle current) {
        this.parent = parent;
        this.current = current;
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
