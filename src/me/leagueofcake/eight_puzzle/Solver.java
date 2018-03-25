package me.leagueofcake.eight_puzzle;

import java.util.*;

/**
 * Solver class for the EightPuzzle.
 *
 * Can solve a puzzle using the following approaches:
 *   - BFS
 *   - Greedy (Manhattan Distance)
 *   - A* (Manhattan Distance + Depth)
 */
public class Solver {
    private final EightPuzzle original;

    /**
     * Constructor for the Solver.
     *
     * @param puzzle - The puzzle state to be solved.
     */
    public Solver (EightPuzzle puzzle) {
        original = new EightPuzzle(puzzle);
    }

    /**
     * Solves the puzzle using a greedy approach (Manhattan distance).
     * @return Whether the puzzle was solved successfully.
     */
    public boolean solveGreedy () {
        return solveWithHeuristic(greedyManhattan());
    }

    /**
     * Solves the puzzle using BFS (breadth-first search).
     *
     * @return Whether the puzzle was solved successfully.
     */
    public boolean solveBFS () {
        return solveWithHeuristic(puzzleNodeDepth());
    }

    /**
     * Solves the puzzle using A* search (depth + Manhattan distance)
     *
     * @return Whether the puzzle was solved successfully.
     */
    public boolean solveAStar () {
        return solveWithHeuristic(aStarManhattan());
    }

    /**
     * Comparator using the Manhattan distance of two PuzzleNodes.
     *
     * @return An integer representing the ordering of the PuzzleNodes.
     */
    private Comparator<PuzzleNode> greedyManhattan () {
        return (p1, p2) -> (p1.current.heuristicManhattan() - p2.current.heuristicManhattan());
    }

    /**
     * Comparator using the depth of two PuzzleNodes.
     *
     * @return An integer representing the ordering of the PuzzleNodes.
     */
    private Comparator<PuzzleNode> puzzleNodeDepth () {
        return (p1, p2) -> (p1.depth - p2.depth);
    }

    /**
     * Comparator using the Manhattan distance and depth of two PuzzleNodes.
     *
     * @return An integer representing the ordering of the PuzzleNodes.
     */
    private Comparator<PuzzleNode> aStarManhattan () {
        return (p1, p2) -> (greedyManhattan().compare(p1, p2) + puzzleNodeDepth().compare(p1, p2));
    }

    /**
     * Solver function that takes in a comparator that determines how the
     * priority queue orders PuzzleNodes.
     *
     * Prints out the number of nodes expanded and the solution from the start to goal state.
     *
     * @param heuristicFunc - comparator function
     * @return Whether the puzzle was solved successfully.
     */
    private boolean solveWithHeuristic (Comparator<PuzzleNode> heuristicFunc) {
        if (!original.isSolvable()) {
            System.out.println("Unsolvable!");
            return false;
        }
        long nodesExpanded = 0;

        Set<EightPuzzle> visited = new HashSet<>();
        PriorityQueue<PuzzleNode> pq = new PriorityQueue<>(heuristicFunc);
        Set<PuzzleNode> queueSet = new HashSet<>();

        PuzzleNode currNode = new PuzzleNode(null, original);
        pq.add(currNode);
        queueSet.add(currNode);

        while (!pq.isEmpty()) {
            currNode = pq.poll();
            queueSet.remove(currNode);
            nodesExpanded++;

            if (currNode.current.isSolved()) break;

            List<EightPuzzle> possibleMoves = currNode.current.generatePossibleMoves();
            for (EightPuzzle e: possibleMoves) {
                PuzzleNode p = new PuzzleNode(currNode, e);
                if (!visited.contains(e) && !queueSet.contains(p)) {
                    pq.add(p);
                    queueSet.add(p);
                }
            }

            visited.add(currNode.current);
        }

        if (currNode.current.isSolved()) {
            System.out.println("Solved!");

            Deque<EightPuzzle> pathStack = generatePath(currNode);
            printSolution(pathStack);

            System.out.println(String.format("Expanded %d nodes.", nodesExpanded));
            System.out.println(String.format("Requires %d moves.", pathStack.size() - 1));
            return true;
        }

        return false;
    }

    /**
     * Generates a Deque representing the solution path. Traverses through
     * each PuzzleNode's parent until the null parent is reached (root node).
     *
     * @param lastNode - the last PuzzleNode returned by the solver.
     * @return A Deque representing the solution path.
     */
    private Deque<EightPuzzle> generatePath (PuzzleNode lastNode) {
        PuzzleNode currNode = lastNode;
        Deque<EightPuzzle> pathStack = new ArrayDeque<>();
        pathStack.add(currNode.current);

        while (currNode.parent != null) {
            currNode = currNode.parent;
            pathStack.addFirst(currNode.current);
        }

        return pathStack;
    }

    /**
     * Given a Deque representing the solution path, prints out each step.
     *
     * @param path - a Deque representing the solution path.
     */
    private void printSolution (Deque<EightPuzzle> path) {
        for (EightPuzzle e: path) {
            System.out.println("Move:");
            System.out.println(e);
            System.out.println();
        }
    }

}
