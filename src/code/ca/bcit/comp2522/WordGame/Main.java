package ca.bcit.comp2522.WordGame;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains main game loop
 *
 * @author David Martinez
 * @version 1.0
 */
public class Main
{
    /**
     * Runner method for the game loop
     * @param args captures user's input to either continue playing game or end program
     */
    public static void main(final String[] args)
    {
        final Scanner sc;
        final List<Score> scores;

        sc     = new Scanner(System.in);
        scores = new ArrayList<>();

        while (sc.hasNextLine())
        {
            // TODO: If user enters "no" any case -> exit game & return to main menu
            if (sc.next().equalsIgnoreCase("no"))
            {
                break;
            }

            if (sc.next().equalsIgnoreCase("yes"))
            {
                //TODO: Run game loop again
                // new score object, etc. At end of game, add score to running
                // List of scores.
                break;
            }

            //TODO: If anything other than "yes" or "no" throw clear error message
            // and repeat the prompt for user
            else
            {
                //TODO: Might replace with constants -> OPTION_ONE and OPTION_TWO
                System.out.println("ERROR: Must enter 'yes' or 'no'");
            }
        }
    }
}
