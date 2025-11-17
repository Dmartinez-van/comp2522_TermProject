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
public class WordGame
{
    private final static int NUM_OF_FACTS = 3;

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
        final List<Country> countries;
        final List<Score> scores;
        final String folderPath;

        folderPath = "./src/code/ca/bcit/comp2522/wordgame/inputs";
        countries = loadCountriesFromFolder(folderPath);
        scores = new ArrayList<>();

        if (countries.isEmpty())
        {
            System.err.println("No countries were loaded from the input files.");
            return;
        }

        // Your game logic here
        countries.forEach(c -> System.out.println(c.toString()));
    }

    /**
     * Loads all countries from text files in the specified folder.
     *
     * @param folderPath the path to the folder containing country data files
     * @return a list of Country objects parsed from the files
     */
    private static List<Country> loadCountriesFromFolder(final String folderPath)
    {
        final List<Country> countries;
        final File folder;
        final File[] files;

        countries = new ArrayList<>();
        folder = new File(folderPath);
        files = folder.listFiles();

        if (files == null || files.length == 0)
        {
            System.err.println("No files found in: " + folder.getAbsolutePath());
            return countries;
        }

        for (final File file : files)
        {
            if (!file.isFile())
            {
                continue;
            }

            try
            {
                final List<Country> fileCountries;
                fileCountries = parseCountriesFromFile(file);
                countries.addAll(fileCountries);
            }
            catch (final FileNotFoundException e)
            {
                System.err.println("File not found: " + e.getMessage());
            }
        }

        return countries;
    }

    /**
     * Parses Country objects from a single file.
     *
     * @param file the file to parse
     * @return a list of Country objects from the file
     * @throws FileNotFoundException if the file cannot be found
     */
    private static List<Country> parseCountriesFromFile(final File file)
            throws FileNotFoundException
    {
        final List<Country> countries;
        final Scanner fileScanner;

        countries = new ArrayList<>();
        fileScanner = new Scanner(file);

        try
        {
            while (fileScanner.hasNextLine())
            {
                final String firstLine;
                firstLine = fileScanner.nextLine();

                if (firstLine.isEmpty())
                {
                    continue;
                }

                final Country country;
                country = parseCountry(firstLine, fileScanner);

                if (country != null)
                {
                    countries.add(country);
                }
            }
        }
        finally
        {
            fileScanner.close();
        }

        return countries;
    }

    /**
     * Parses a single Country object from the scanner.
     *
     * @param firstLine the line containing "CountryName:CapitalCity"
     * @param scanner the scanner to read facts from
     * @return a Country object, or null if parsing fails
     */
    private static Country parseCountry(final String firstLine,
                                        final Scanner scanner)
    {
        final String[] countryData;
        final Country  country;
        final String   countryName;
        final String   capitalCityName;
        final String[] facts;
        final StringBuilder factBuilder;

        int factPointer;

        countryData = firstLine.split(":");

        if (countryData.length != 2)
        {
            return null;
        }

        countryName     = countryData[0].trim();
        capitalCityName = countryData[1].trim();
        facts           = new String[NUM_OF_FACTS];
        factBuilder     = new StringBuilder();
        factPointer     = 0;

        while (scanner.hasNextLine())
        {
            final String line;
            line = scanner.nextLine().trim();

            if (line.isEmpty())
            {
                break;
            }

            factBuilder.append(line);

            if (line.endsWith("."))
            {
                facts[factPointer] = factBuilder.toString();
                factBuilder.setLength(0);
                factPointer++;
            }
        }

        country = new Country(countryName,
                              capitalCityName,
                              facts);
        return country;
    }
}
