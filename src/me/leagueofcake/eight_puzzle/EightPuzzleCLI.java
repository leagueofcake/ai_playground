package me.leagueofcake.eight_puzzle;

import java.util.*;

/**
 * CLI interface for interacting with the EightPuzzle class.
 *
 * One EightPuzzle can be loaded at any given time (tracked by the property currentPuzzle). Initially no
 * puzzle is loaded.
 *
 * The DEBUG flag prints additional debugging information - set as required.
 *
 * A detailed list of commands can be found by inputting ? on the CLI.
 */
public class EightPuzzleCLI {
    private EightPuzzle currentPuzzle;
    private boolean running;
    private static final boolean DEBUG = true;

    /**
     * Initialises and starts the CLI
     * @param args Unused
     */
    public static void main (String[] args) {
        EightPuzzleCLI cli = new EightPuzzleCLI();
        cli.run();
    }

    /**
     * Constructor for the CLI
     */
    public EightPuzzleCLI () {
        if (DEBUG) System.out.println("Constructing EightPuzzleCLI()...");
        currentPuzzle = null;
        running = true;
    }

    /**
     * Command handler for the CLI.
     *
     * When the CLI is running, type ? to see details for each command.
     */
    private void run () {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            System.out.print("Command: ");
            String command = scanner.nextLine().trim();
            switch (command) {
                case "gen":
                    generatePuzzle(); break;
                case "u":
                case "d":
                case "l":
                case "r":
                    moveBoard(command); break;
                case "i":
                    inputBoard(scanner); break;
                case "p":
                    printBoard(); break;
                case "q":
                    quit(); break;
                case "?":
                    help(); break;
                case "s":
                    printSolved(); break;
                case "bfs":
                    solveBFS(); break;
                case "a*":
                    solveAStar(); break;
                case "greedy":
                    solveGreedy(); break;
                case "chk":
                    checkSolvable(); break;
                default:
                    System.out.println("Unknown command. Type ? to see the commands list.");
                    break;
            }
        }
    }

    /**
     * Basic movement handler using u/d/l/r (up/down/left/right).
     *
     * @param movement A string representing the direction to move to.
     */
    private void moveBoard (String movement) {
        if (checkPuzzleLoaded()) {
            switch (movement) {
                case "u":
                    currentPuzzle.moveUp(); break;
                case "d":
                    currentPuzzle.moveDown(); break;
                case "l":
                    currentPuzzle.moveLeft(); break;
                case "r":
                    currentPuzzle.moveRight(); break;
            }
            printBoard();
        }
    }

    /**
     * Handles inputting a board from the command line. On success,
     * currentPuzzle is set to the newly created board.
     *
     * Reads PUZZLE_WIDTH numbers on each line, with PUZZLE_HEIGHT lines.
     * If any input line is invalid (incorrect number of numbers
     * on line/non-integers found), re-requests the line.
     *
     * @param scanner Scanner object
     */
    private void inputBoard (Scanner scanner) {
        System.out.println(String.format("Type %s numbers per line (%s - %s), separated by spaces.",
                EightPuzzle.PUZZLE_WIDTH, 1, EightPuzzle.PUZZLE_WIDTH * EightPuzzle.PUZZLE_HEIGHT - 1));
        System.out.println("Use 0 to denote the space in the board.");

        int[][] inputBoard = new int[EightPuzzle.PUZZLE_HEIGHT][EightPuzzle.PUZZLE_WIDTH];
        for (int row = 0; row < EightPuzzle.PUZZLE_HEIGHT; row++) {
            String[] splitInput = scanner.nextLine().trim().split("\\s+");
            if (splitInput.length != EightPuzzle.PUZZLE_WIDTH) {
                System.out.println("Error: invalid input size for the row");
                row--;
            } else {
                for (int col = 0; col < EightPuzzle.PUZZLE_WIDTH; col++) {
                    try {
                        int num = Integer.parseInt(splitInput[col]);
                        inputBoard[row][col] = num;
                    } catch (NumberFormatException e) {
                        System.out.println("Non-integers found in input!");
                        row--;
                        break;
                    }
                }
            }
        }

        currentPuzzle = new EightPuzzle(inputBoard);
        if (currentPuzzle.isValidBoard()) {
            printBoard();
        } else {
            System.out.println("Invalid board!");
            currentPuzzle = null;
        }
    }

    /**
     * Prints out the board, if one is loaded.
     */
    private void printBoard () {
        if (checkPuzzleLoaded()) {
            System.out.println(currentPuzzle);
        }
    }

    /**
     * Prints out whether the current board is solved, if one is loaded.
     */
    private void printSolved () {
        if (checkPuzzleLoaded()) {
            System.out.println(currentPuzzle.isSolved());
        }
    }

    /**
     * Prints out the command list and information on each command.
     */
    private void help () {
        String helpText = "Commands List\n" +
                "gen    Generate a solvable puzzle and sets current puzzle to it\n" +
                "i      Input a puzzle from the command line\n" +
                "u      Move the space up\n" +
                "d      Move the space down\n" +
                "l      Move the space left\n" +
                "r      Move the space right\n" +
                "bfs    Solves the puzzle using BFS\n" +
                "greedy Solves the puzzle using a greedy approach with Manhattan distance heuristic\n" +
                "a*     Solves the puzzle using A* search with Manhattan distance heuristic\n" +
                "p      Prints the currently loaded board\n" +
                "s      Prints whether the board is solved\n" +
                "chk    Prints whether the current board is solvable\n" +
                "?      Displays this command list\n" +
                "q      Quits the program\n";
        System.out.println(helpText);
    }

    /**
     * Solves the current board using BFS, if one is loaded.
     */
    private void solveBFS () {
        if (checkPuzzleLoaded()) {
            Solver solver = new Solver(currentPuzzle);
            solver.solveBFS();
        }
    }

    /**
     * Solve the current board using A*, if one is loaded.
     */
    private void solveAStar () {
        if (checkPuzzleLoaded()) {
            Solver solver = new Solver(currentPuzzle);
            solver.solveAStar();
        }
    }

    /**
     * Solves the current board using a greedy algorithm if one is loaded.
     */
    private void solveGreedy () {
        if (checkPuzzleLoaded()) {
            Solver solver = new Solver(currentPuzzle);
            solver.solveGreedy();
        }
    }

    /**
     * Checks whether a puzzle is loaded, printing an error message if not.
     *
     * @return Whether a puzzle is loaded.
     */
    private boolean checkPuzzleLoaded () {
        if (currentPuzzle == null) {
            System.out.println("No puzzle loaded!");
            return false;
        }
        return true;
    }

    /**
     * Randomly generates puzzles until a solvable one is generated.
     *
     * Sets currentPuzzle to the newly generated board.
     */
    private void generatePuzzle () {
        int[][] board = new int[EightPuzzle.PUZZLE_HEIGHT][EightPuzzle.PUZZLE_WIDTH];

        // List containing numbers 0 - (HEIGHT * WIDTH - 1)
        List<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < EightPuzzle.PUZZLE_WIDTH * EightPuzzle.PUZZLE_HEIGHT; i++) permutation.add(i);

        EightPuzzle result;
        Set<EightPuzzle> generated = new HashSet<>();

        // Generate puzzles until we find a solvable one
        do {
            // Re-shuffle permutation until we find one that hasn't already been generated
            do {
                Collections.shuffle(permutation);

                // Fill board with numbers in permutation
                for (int row = 0; row < EightPuzzle.PUZZLE_HEIGHT; row++) {
                    for (int col = 0; col < EightPuzzle.PUZZLE_WIDTH; col++) {
                        board[row][col] = permutation.get((row * EightPuzzle.PUZZLE_WIDTH) + col);
                    }
                }

                result = new EightPuzzle(board);
            } while (generated.contains(result));

            generated.add(result);
        } while (!result.isSolvable());

        currentPuzzle = result;
        printBoard();
    }

    /**
     * Prints out whether the current puzzle is solvable.
     */
    private void checkSolvable () {
        String result = currentPuzzle.isSolvable() ? "Solvable!" : "Unsolvable!";
        System.out.println(result);
    }

    /**
     * Quits the program.
     */
    private void quit () {
        if (DEBUG) System.out.println("Quitting...");
        running = false;
    }
}
