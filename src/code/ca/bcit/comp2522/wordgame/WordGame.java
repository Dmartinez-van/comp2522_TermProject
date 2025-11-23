package ca.bcit.comp2522.wordgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.random.RandomGenerator;

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
    private final static int NUM_QUESTIONS           = 3;

    protected final static int LOWER_BOUND       = 0;
    protected final static int UPPER_BOUND       = 3;
    protected final static int TYPE_CAPITAL_CITY = 0;
    protected final static int TYPE_COUNTRY_NAME = 1;
    protected final static int TYPE_FACT         = 2;

    private final List<Question> questions;
    private final List<Country>  countries;
    private final Score          gameScore;
    private final List<Score>    scores;

    private final Scanner scanner;

    /**
     * Constructs a WordGame object and initializes game data.
     *
     * @param scanner the scanner for user input
     * @throws IOException if input files cannot be read
     */
    public WordGame(final Scanner scanner) throws IOException
    {
        final List<Score> scores;
        final Path inputsPath;
        final LocalDateTime datePlayed;

        questions    = new ArrayList<>();
        scores       = new ArrayList<>();
        datePlayed   = LocalDateTime.now();
        this.scores  = scores;
        this.scanner = scanner;
        gameScore    = new Score(datePlayed,
                                 NONE,
                                 NONE,
                                 NONE,
                                 NONE);

        inputsPath = Paths.get(
            "src",
            "code",
            "ca",
            "bcit",
            "comp2522",
            "wordgame",
            "inputs");

        countries = loadCountriesFromFolder(inputsPath);

        if (countries.isEmpty())
        {
            System.err.println("No countries were loaded from the input files.");
            return;
        }

        // Get previous game scores, for later comparison
        scores.addAll(Score.readScoresFromFile("scores.txt"));

        // Get previous high score before playing
        final Score previousHighScore;
        previousHighScore = scores.stream()
                                  .filter(s -> s.getNumGamesPlayed() > NONE)
                                  .max(Comparator.comparingDouble(s -> (double) s.getScore() / s.getNumGamesPlayed()))
                                  .orElse(null);

        // Run game
        startLoop();

        // append score to file
        final double averageScore;
        final double previousAverageScore;

        averageScore         = (double) gameScore.getScore() / gameScore.getNumGamesPlayed();
        previousAverageScore = previousHighScore == null ? NONE :
            (double) previousHighScore.getScore() / previousHighScore.getNumGamesPlayed();

        if (averageScore > previousAverageScore)
        {
            System.out.println("Congratulations! You achieved a new high score of " +
                               gameScore.getScore() + " points per game!");
        }
        else
        {
            assert previousHighScore != null;
            System.out.println("You did not beat the \"high score\" of " +
                               previousAverageScore +
                               " points per game from " +
                               previousHighScore.getDateTimePlayed() +
                               ".");
        }

        Score.appendScoreToFile(gameScore, "scores.txt");
    }

    /**
     * Starts the main game loop, asking questions and collecting scores.
     */
    private void startLoop()
    {
        boolean playAgain;

        do
        {
            gameScore.incrementGamesPlayed();

            // Generate questions, store in questions list
            questions.clear();
            questions.addAll(generateQuestions(countries));

            for (final Question question : questions)
            {
                System.out.println("\n" + question.getPrompt());
                System.out.print("Your answer: ");

                final String userAnswer;
                userAnswer = scanner.nextLine().trim();

                if (userAnswer.equalsIgnoreCase(question.getAnswer()))
                {
                    System.out.println("CORRECT!");
                    gameScore.incrementFirstCorrectAnswers();
                }
                else
                {
                    System.out.print("INCORRECT\nTry again: ");

                    final String secondAttempt;
                    secondAttempt = scanner.nextLine().trim();

                    if (secondAttempt.equalsIgnoreCase(question.getAnswer()))
                    {
                        System.out.println("CORRECT!");
                        gameScore.incrementSecondCorrectAnswers();
                    }
                    else
                    {
                        System.out.println("INCORRECT! The correct answer was: " + question.getAnswer());
                        gameScore.incrementIncorrectAnswers();
                    }
                }
            }

            System.out.println(gameScore.getCorrectAnswers());
            scores.add(gameScore);

            System.out.print("Do you want to play again? (yes/no): ");

            String response;
            while (true)
            {
                response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("yes") ||
                    response.equals("no"))
                {
                    break;
                }

                System.out.print("Invalid input, please enter yes or no: ");
            }

            playAgain = response.equals("yes");
        } while (playAgain);
    }

    /**
     * Generates a list of questions for the game.
     *
     * @param countries the list of countries to generate questions from
     * @return a list of Question objects
     */
    private List<Question> generateQuestions(final List<Country> countries)
    {
        final List<Question> questions;
        final RandomGenerator rng;
        rng = RandomGenerator.getDefault();

        Collections.shuffle(countries);
        questions = countries.stream()
                             .limit(NUM_QUESTIONS)
                             .map(country -> new Question(rng.nextInt(LOWER_BOUND, UPPER_BOUND), country))
                             .toList();

        return questions;
    }

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
