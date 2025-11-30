package ca.bcit.comp2522.mygame;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * Manages loading and saving of high scores for the game.
 *
 * @author David Martinez
 * @version 1.0
 */
public final class HighScoreManager
{
    private static final Path HIGH_SCORE_PATH    = Paths.get("src",
                                                             "code",
                                                             "ca",
                                                             "bcit",
                                                             "comp2522",
                                                             "mygame",
                                                             "highscore.txt");
    private static final int  DEFAULT_HIGH_SCORE = 0;

    /**
     * Private constructor to prevent instantiation.
     */
    private HighScoreManager()
    {
    }

    /**
     * Load the high score from a file.
     *
     * @return the loaded high score, or default if not found or error occurs
     */
    public static int load()
    {
        System.out.println("Loading high score from " + HIGH_SCORE_PATH + ".");
        if (Files.notExists(HIGH_SCORE_PATH))
        {
            return DEFAULT_HIGH_SCORE;
        }

        try
        {
            final String content;
            content = Files.readString(HIGH_SCORE_PATH).trim();
            return Integer.parseInt(content);
        }
        catch (final Exception e)
        {
            System.out.println("Failed to read high score file. Defaulting to " + DEFAULT_HIGH_SCORE + ".");

            return DEFAULT_HIGH_SCORE;
        }
    }

    /**
     * Save the high score to a file.
     *
     * @param score the high score to save
     */
    public static void save(final int score)
    {
        try
        {
            final Path parent;
            parent = HIGH_SCORE_PATH.getParent();
            System.out.println("~~~~");
            System.out.println("parent: " + parent);
            System.out.println("Saving high score to " + HIGH_SCORE_PATH + ".");
            if (parent != null && Files.notExists(parent))
            {
                System.out.println("Creating directories for high score at " + parent + ".");
                Files.createDirectories(parent);
            }

            Files.writeString(HIGH_SCORE_PATH,
                              Integer.toString(score),
                              StandardOpenOption.CREATE,
                              StandardOpenOption.TRUNCATE_EXISTING
                             );
        }
        catch (final Exception e)
        {
            System.out.println("Failed to save high score.");
        }
    }
}
