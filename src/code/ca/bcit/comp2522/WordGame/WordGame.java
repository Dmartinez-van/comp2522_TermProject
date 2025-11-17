package ca.bcit.comp2522.WordGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final static int NUM_OF_FACTS            = 3;
    private final static int MIN_NUM_OF_COUNTRY_DATA = 2;
    private final static int NONE                    = 0;

    /**
     * Runner method for the game loop.
     * First, load all game inputs into the appropriate data structures
     * Second, initiate game loop, checking for user input to begin another round or end program
     *
     * @param args captures user's input to either continue playing game or end program
     * @throws FileNotFoundException if input file is not found
     */
    public static void main(final String[] args) throws IOException
    {
        final List<Country> countries;
        final List<Score> scores;

        final Path inputsPath;

        inputsPath = Paths.get(
            "src",
            "code",
            "ca",
            "bcit",
            "comp2522",
            "wordgame",
            "inputs");

        countries = loadCountriesFromFolder(inputsPath);
        scores    = new ArrayList<>();

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
     * @param inputsPath the path to the folder containing country data files
     * @return a list of Country objects parsed from the files
     */
    private static List<Country> loadCountriesFromFolder(final Path inputsPath) throws IOException
    {
        final List<Country> countries;

        countries = new ArrayList<>();

        // Protect against non-exiting inputs folder
        if (Files.notExists(inputsPath))
        {
            Files.createDirectories(inputsPath);
            System.err.println("Created inputs folder: " + inputsPath.toAbsolutePath());
            return countries; // empty list, nothing to load yet
        }

        // Protect against non-directory 'inputs' filename
        if (!Files.isDirectory(inputsPath))
        {
            throw new IOException("Input folder is not a directory." + inputsPath.toAbsolutePath());
        }

        final File folder;
        final File[] files;

        folder = inputsPath.toFile();
        files  = folder.listFiles();

        // Protect against empty inputs directory
        if (files == null || files.length == NONE)
        {
            System.out.println("No files were found in the input folder.");
            return countries;
        }

        // Read each file, create new Country object for each
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

        countries   = new ArrayList<>();
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
     * Parses and creates a single Country object from the scanner.
     *
     * @param firstLine the line containing "CountryName:CapitalCity"
     * @param scanner   the scanner to read facts from
     * @return a Country object, or null if parsing fails
     */
    private static Country parseCountry(final String firstLine,
                                        final Scanner scanner)
    {
        final String[] countryData;
        final Country country;
        final String countryName;
        final String capitalCityName;
        final String[] facts;
        final StringBuilder factBuilder;
        final int initialIndex;
        final int countryNameIndex;
        final int capitalCityNameIndex;

        initialIndex         = 0;
        countryNameIndex     = initialIndex;
        capitalCityNameIndex = countryNameIndex + 1;

        int factPointer;

        countryData = firstLine.split(":");

        if (countryData.length != MIN_NUM_OF_COUNTRY_DATA)
        {
            return null;
        }

        countryName     = countryData[countryNameIndex].trim();
        capitalCityName = countryData[capitalCityNameIndex].trim();
        facts           = new String[NUM_OF_FACTS];
        factBuilder     = new StringBuilder();
        factPointer     = initialIndex;

        while (scanner.hasNextLine())
        {
            final String trimmedLine;
            trimmedLine = scanner.nextLine().trim();

            if (trimmedLine.isEmpty())
            {
                break;
            }

            if (!factBuilder.isEmpty())
            {
                factBuilder.append(' ');
            }
            factBuilder.append(trimmedLine);

            if (trimmedLine.endsWith("."))
            {
                facts[factPointer] = factBuilder.toString();
                factBuilder.setLength(NONE);
                factPointer++;
            }
        }

        // Stores rest of text into last fact in case it didn't end with a "."
        if (!factBuilder.isEmpty() && factPointer < NUM_OF_FACTS)
        {
            facts[factPointer] = factBuilder.toString();
        }

        country = new Country(countryName,
                              capitalCityName,
                              facts);
        return country;
    }
}
