package ca.bcit.comp2522.numbergame;

import javafx.application.Application;

/**
 * Launcher class for the Number Game JavaFX application.
 * Only allows a single launch per program run.
 *
 * @author David Martinez
 * @version 1.0
 */
public class NumberGameLauncher
{
    private static boolean launched = false;

    /**
     * Private constructor to prevent instantiation.
     */
    private NumberGameLauncher(){}

    /**
     * Launcher method for the Number Game JavaFX application.
     */
    public static void start()
    {
        if (launched)
        {
            System.out.println("Number Game can only be launched once per run.\nIf you have complaints about this, please bring it up with Mr. JavaFX.");
            return;
        }

        launched = true;
        Application.launch(NumberGameApp.class);
    }
}
