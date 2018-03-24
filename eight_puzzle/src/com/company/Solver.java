package com.company;

import java.util.*;

public class Solver {
    EightPuzzle original;

    public Solver (EightPuzzle puzzle) {
        original = new EightPuzzle(puzzle);
    }

    public void printSolution (Deque<EightPuzzle> path) {
        for (EightPuzzle e: path) {
            System.out.println("Move:");
            System.out.println(e);
            System.out.println();
        }
    }

    public void solveBFS () {
        long nodesExpanded = 0;

        Set<EightPuzzle> visited = new HashSet<>();
        Queue<PuzzleNode> queue = new LinkedList<>();
        Set<PuzzleNode> queueSet = new HashSet<>();

        PuzzleNode currNode = new PuzzleNode(null, original);
        queue.add(currNode);
        queueSet.add(currNode);

        while (!queue.isEmpty()) {
            currNode = queue.remove();
            nodesExpanded++;

            if (currNode.current.isSolved()) break;

            List<EightPuzzle> possibleMoves = currNode.current.generatePossibleMoves();
            for (EightPuzzle e: possibleMoves) {
                PuzzleNode p = new PuzzleNode(currNode, e);
                if (!visited.contains(e) && !queueSet.contains(p)) {
                    queue.add(p);
                    queueSet.add(p);
                }
            }

            visited.add(currNode.current);
        }
        System.out.println("Solved!");

        Deque<EightPuzzle> pathStack = new ArrayDeque<>();
        pathStack.add(currNode.current);

        while (currNode.parent != null) {
            currNode = currNode.parent;
            pathStack.addFirst(currNode.current);
        }
        printSolution(pathStack);
        System.out.println(String.format("Expanded %d nodes.", nodesExpanded));
    }
}
