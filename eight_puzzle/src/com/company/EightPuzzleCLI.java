package com.company;

import java.util.Scanner;

public class EightPuzzleCLI {
    private EightPuzzle currentPuzzle;
    private boolean running;
    private static final boolean DEBUG = true;

    public EightPuzzleCLI () {
        if (DEBUG) System.out.println("Constructing EightPuzzleCLI()...");
        currentPuzzle = null;
        running = true;
    }

    public static void main (String[] args) {
        EightPuzzleCLI cli = new EightPuzzleCLI();
        cli.run();
    }

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
                default:
                    System.out.println("Unknown command. Type ? to see the commands list.");
                    break;
            }
        }
    }

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

    private void printBoard () {
        if (currentPuzzle == null) {
            System.out.println("No puzzle loaded!");
        } else {
            System.out.println(currentPuzzle);
        }
    }

    private void help () {
        String helpText = "Commands List\n" +
                "i    Input a puzzle from the command line\n" +
                "p    Prints the currently loaded board\n" +
                "?    Displays this command list\n" +
                "q    Quits the program\n";
        System.out.println(helpText);
    }

    private void quit () {
        if (DEBUG) System.out.println("Quitting...");
        running = false;
    }
}
