package ca.bcit.comp2522.mygame;

import javafx.application.Application;

/**
 * Launcher class for Auto Clicker Application.
 *
 * @author David Martinez
 * @version 1.0
 */
public class AutoClickerLauncher
{
    /** Private constructor to prevent instantiation. */
    private AutoClickerLauncher()
    {
    }

    /**
     * Starts the Auto Clicker Application.
     */
    public static void start()
    {
        Application.launch(AutoClickerApp.class);
    }
}
