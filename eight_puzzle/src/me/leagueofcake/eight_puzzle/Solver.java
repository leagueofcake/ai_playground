package me.leagueofcake.eight_puzzle;

import java.util.*;

public class Solver {
    EightPuzzle original;

    public Solver (EightPuzzle puzzle) {
        original = new EightPuzzle(puzzle);
    }

    public boolean checkSolveable () {
        return original.isSolveable();
    }

    public boolean solveGreedy (boolean quiet) {
        return solveWithHeuristic(greedyManhattan(), quiet);
    }

    public boolean solveBFS (boolean quiet) {
        return solveWithHeuristic(puzzleNodeDepth(), quiet);
    }

    public boolean solveAStar (boolean quiet) {
        return solveWithHeuristic(aStarManhattan(), quiet);
    }

    private Comparator<PuzzleNode> greedyManhattan () {
        return (p1, p2) -> (p1.current.heuristicManhattan() - p2.current.heuristicManhattan());
    }

    private Comparator<PuzzleNode> puzzleNodeDepth () {
        return (p1, p2) -> (p1.depth - p2.depth);
    }

    private Comparator<PuzzleNode> aStarManhattan () {
        return (p1, p2) -> (greedyManhattan().compare(p1, p2) + puzzleNodeDepth().compare(p1, p2));
    }

    private boolean solveWithHeuristic (Comparator<PuzzleNode> heuristicFunc, boolean quiet) {
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
            if (!quiet) {
                System.out.println("Solved!");

                Deque<EightPuzzle> pathStack = generatePath(currNode);
                printSolution(pathStack);

                System.out.println(String.format("Expanded %d nodes.", nodesExpanded));
                System.out.println(String.format("Requires %d moves.", pathStack.size() - 1));
            }

            return true;
        } else {
            if (!quiet) {
                System.out.println("Unsolveable!");
            }
            return false;
        }
    }

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

    private void printSolution (Deque<EightPuzzle> path) {
        for (EightPuzzle e: path) {
            System.out.println("Move:");
            System.out.println(e);
            System.out.println();
        }
    }

}
