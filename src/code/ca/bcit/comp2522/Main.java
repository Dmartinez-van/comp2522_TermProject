package ca.bcit.comp2522;

import ca.bcit.comp2522.mygame.AutoClickerLauncher;
import ca.bcit.comp2522.numbergame.NumberGameLauncher;
import ca.bcit.comp2522.wordgame.WordGame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Starts game loop for selecting a game type
 * (M or m) for My Game
 * (N or n) for Number Game
 * (W or w) for Word Game
 * (Q or q) to exit program
 *
 * @author David Martinez
 * @version 1.0
 */
public class Main
{
    private static final String BOLD  = "\u001B[1m";
    private static final String RESET = "\u001B[0m";
    private static final int FIRST_CHAR_INDEX  = 0;
    private static final int SECOND_CHAR_INDEX = 1;

    /**
     * Displays the game selection menu to the user.
     */
    private static void displayMenu()
    {
        System.out.println("\n=== GAME SELECTION MENU ===");
        System.out.println("(M) My Game");
        System.out.println("(N) Number Game");
        System.out.println("(W) Word Game");
        System.out.println("(Q) Quit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Runner method for main game selection loop
     *
     * @param args user input
     * @throws FileNotFoundException if no file is found during reading
     */
    public static void main(final String[] args) throws IOException
    {
        final Scanner scanner;
        boolean running;

        scanner = new Scanner(System.in);
        running = true;

        while (running)
        {
            displayMenu();

            final String selection;
            selection = scanner.nextLine()
                               .trim()
                               .substring(FIRST_CHAR_INDEX, SECOND_CHAR_INDEX)
                               .toLowerCase();

            switch (selection)
            {
                case "m" ->
                {
                    AutoClickerLauncher.start();
                }
                case "n" ->
                {
                    NumberGameLauncher.start();
                }
                case "w" ->
                {
                    new WordGame(scanner);
                }
                case "q" ->
                {
                    System.out.println("Exiting program. Goodbye!");
                    running = false;
                }

                default -> System.out.println(BOLD + "Invalid selection. Please choose M, N, W, or Q." + RESET);
            }
        }

        scanner.close();
    }
}
