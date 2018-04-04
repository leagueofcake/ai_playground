package me.leagueofcake.sudoku;

import java.util.*;

/**
 * CLI interface for interacting with the Sudoku class.
 *
 * One Sudoku puzzle can be loaded at any given time (tracked by the property currentPuzzle). Initially no
 * puzzle is loaded.
 *
 * The DEBUG flag prints additional debugging information - set as required.
 *
 * A detailed list of commands can be found by inputting ? on the CLI.
 */
public class SudokuCLI {
    private SudokuBoard currentPuzzle;
    private boolean running;
    private static final boolean DEBUG = true;

    /**
     * Initialises and starts the CLI
     * @param args Unused
     */
    public static void main (String[] args) {
        SudokuCLI cli = new SudokuCLI();
        cli.run();
    }

    /**
     * Constructor for the CLI
     */
    public SudokuCLI() {
        if (DEBUG) System.out.println("Constructing SudokuCLI()...");
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
                case "i":
                    inputBoard(scanner); break;
                case "p":
                    printBoard(); break;
                case "q":
                    quit(); break;
                case "?":
                    help(); break;
                case "solve":
                    solve(); break;
                default:
                    System.out.println("Unknown command. Type ? to see the commands list.");
                    break;
            }
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
        System.out.println(String.format("Type %s characters per line (%s - %s), or . for an empty space.",
                SudokuBoard.BOARD_SIZE, 1, SudokuBoard.BOARD_SIZE));

        char[][] inputBoard = new char[SudokuBoard.BOARD_SIZE][SudokuBoard.BOARD_SIZE];
        for (int row = 0; row < SudokuBoard.BOARD_SIZE; row++) {
            String splitInput = scanner.nextLine().trim();
            if (splitInput.length() != SudokuBoard.BOARD_SIZE) {
                System.out.println("Error: invalid input size for the row");
                row--;
            } else {
                for (int col = 0; col < SudokuBoard.BOARD_SIZE; col++) {
                    char c = splitInput.charAt(col);
                    if (c == '.' || (1 <= c - '0' && c - '0' <= 9)) {
                        inputBoard[row][col] = c;
                    } else {
                        System.out.println("Non-integers found in input!");
                        row--;
                    }
                }
            }
        }

        currentPuzzle = new SudokuBoard(inputBoard);
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
     * Prints out the command list and information on each command.
     */
    private void help () {
        String helpText = "Commands List\n" +
                "i      Input a Sudoku board from the command line\n" +
                "p      Prints the currently loaded board\n" +
                "solve  Attempts to solve the Sudoku puzzle\n" +
                "?      Displays this command list\n" +
                "q      Quits the program\n";
        System.out.println(helpText);
    }

    /**
     * Solves the current board.
     */
    private void solve () {
        if (checkPuzzleLoaded()) {
            Solver solver = new Solver(currentPuzzle);
            solver.solveSudoku();
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
     * Quits the program.
     */
    private void quit () {
        if (DEBUG) System.out.println("Quitting...");
        running = false;
    }
}
