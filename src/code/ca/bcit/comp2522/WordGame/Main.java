package ca.bcit.comp2522.WordGame;

import java.io.File;
import java.io.FileNotFoundException;
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
     * Runner method for the game loop.
     * First, load all game inputs into the appropriate data structures
     * Second, initiate game loop, checking for user input to begin another round or end program
     *
     * @param args captures user's input to either continue playing game or end program
     * @throws FileNotFoundException if input file is not found
     */
    public static void main(final String[] args) throws FileNotFoundException
    {
        // Setup file reader
        final String  folderPath;
        final File    folder;
        final File[]  files;

        // Setup scores list
        final List<Score> scores;

        // Instantiate file reading
        folderPath = "./src/code/ca/bcit/comp2522/wordgame/inputs";
        folder     = new File(folderPath);
        files      = folder.listFiles();

        // Instantiate scores array
        scores = new ArrayList<>();

        // Immediately exit program if there are no input files to populate game with
        if (files == null || files.length == 0)
        {
            System.err.println("No files found in: " + folder.getAbsolutePath());
            return;
        }

        for (final File file : files)
        {
            if (!file.isFile())
            {
                continue;
            }

            // Process file
            final Scanner fileScanner;
            fileScanner = new Scanner(file);

            try
            {
                while (fileScanner.hasNextLine())
                {
                    String line = fileScanner.nextLine();
                    //TODO: Parse each line and save data to correct place.
                }
            }
            catch (final RuntimeException e)
            {
                System.err.println("File not found: " + e.getMessage());
            }

            // End loading input files
            fileScanner.close();
        }
    }
}
