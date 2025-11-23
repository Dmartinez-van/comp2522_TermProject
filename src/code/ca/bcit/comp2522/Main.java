package ca.bcit.comp2522;

import ca.bcit.comp2522.WordGame.WordGame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Starts game loop for selecting a game type
 * (M) for My Game
 * (N) for Number Game
 * (W) for Word Game
 * (Q) to exit program
 *
 * @author David Martinez
 * @version 1.0
 */
public class Main
{
    private static final String BOLD  = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

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
            final int firstIndex;
            final int secondIndex;

            firstIndex  = 0;
            secondIndex = 1;

            displayMenu();

            final String selection;
            selection = scanner.nextLine()
                               .trim()
                               .substring(firstIndex, secondIndex)
                               .toLowerCase();

            switch (selection)
            {
                case "m" ->
                {
                    System.out.println("Starting My Game...");
                    ca.bcit.comp2522.MyGame.Main.main(new String[firstIndex]);

                    running = false;
                }

                case "n" ->
                {
                    System.out.println("Starting Number Game...");
                    ca.bcit.comp2522.NumberGame.Main.main(new String[firstIndex]);

                    running = false;
                }

                case "w" ->
                {
                    new WordGame(scanner);

                    running = false;
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
