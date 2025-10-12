package ca.bcit.comp2522;

import java.io.FileNotFoundException;
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
    public static void main(final String[] args) throws FileNotFoundException
    {
        final Scanner scanner;
        boolean running;

        scanner = new Scanner(System.in);
        running = true;

        while (running)
        {
            displayMenu();
            final String selection;
            selection = scanner.next()
                            .substring(0, 1)
                            .toLowerCase();

            switch (selection)
            {
                case "m" ->
                {
                    System.out.println("Starting My Game...");
                    ca.bcit.comp2522.MyGame.Main.main(new String[0]);
                }

                case "n" ->
                {
                    System.out.println("Starting Number Game...");
                    ca.bcit.comp2522.NumberGame.Main.main(new String[0]);
                }

                case "w" ->
                {
                    System.out.println("Starting Word Game...");
                    ca.bcit.comp2522.WordGame.Main.main(new String[0]);
                }

                case "q" ->
                {
                    System.out.println("Exiting program. Goodbye!");
                    running = false;
                }

                default ->
                    System.out.println(BOLD + "Invalid selection. Please choose M, N, W, or Q." + RESET);

            }
        }

        scanner.close();
    }
}
