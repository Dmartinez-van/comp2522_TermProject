package ca.bcit.comp2522.WordGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Score class
 *
 * @author David Martinez
 * @version 1.0
 */
final public class Score
{
    private final static int CORRECT_FIRST_GUESS_PTS  = 2;
    private final static int CORRECT_SECOND_GUESS_PTS = 1;
    private final static int INCORRECT_GUESS_PTS      = 0;
    private final static int NONE                     = 0;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static int MAX_GUESSES = 2;

    private final        LocalDateTime     dateTimePlayed;

    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectTwoAttempts;

    /**
     * Full constructor
     *
     * @param numGamesPlayed          The number of full games played
     * @param numCorrectFirstAttempt  The number of correct answers on the
     *                                user's first attempt
     * @param numCorrectSecondAttempt The number of correct answers on the
     *                                user's second attempt
     * @param numIncorrectTwoAttempts The number of incorrect answers after
     *                                the user attempts twice
     */
    public Score(final LocalDateTime dateTimePlayed,
                 final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectTwoAttempts)
    {
        validate(dateTimePlayed);
        validate(numGamesPlayed);
        validate(numCorrectFirstAttempt);
        validate(numCorrectSecondAttempt);
        validate(numIncorrectTwoAttempts);

        this.dateTimePlayed          = dateTimePlayed;
        this.numGamesPlayed          = numGamesPlayed;
        this.numCorrectFirstAttempt  = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;
    }

    /*
    Validates for bad dates
    Throws IllegalArgumentException if fails check(s)
    */
    private void validate(final LocalDateTime dateTimePlayed)
    {
        if (dateTimePlayed == null)
        {
            throw new IllegalArgumentException("Date time played cannot be null");
        }
    }

    /*
    Validates for bad integers
    Throws IllegalArgumentException if fails check(s)
    */
    private void validate(final int value)
    {
        if (value < NONE)
        {
            throw new IllegalArgumentException("Value cannot be less than " + NONE);
        }
    }

    /**
     * Write score to a specified file
     *
     * @param score     the score to write to file
     * @param scoreFile the output file name
     */
    public static void appendScoreToFile(final Score score,
                                         final String scoreFile)
    {
        final Path path;
        final String line;

        path = Paths.get(scoreFile);
        line = formatScoreLine(score) + System.lineSeparator();

        try
        {
            Files.writeString(path,
                              line,
                              StandardOpenOption.CREATE,
                              StandardOpenOption.APPEND);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }


    }

    /**
     * Formatter for writing scores to file.
     *
     * @param score the score
     * @return a string for writing
     */
    private static String formatScoreLine(final Score score)
    {
        final StringBuilder builder;

        builder = new StringBuilder();

        builder.append(score.getDateTimePlayed());
        builder.append(", ");
        builder.append(score.getNumGamesPlayed());
        builder.append(", ");
        builder.append(score.getNumCorrectFirstAttempt());
        builder.append(", ");
        builder.append(score.getNumCorrectSecondAttempt());
        builder.append(", ");
        builder.append(score.getNumIncorrectTwoAttempts());

        return builder.toString();
    }

    /**
     * Read scores from file
     *
     * @param scoreFile the file to read scores from
     * @return a {@code List<Score>} containing scores from a file
     */
    public static List<Score> readScoresFromFile(final String scoreFile)
    {
        final Path path;
        final List<Score> scores;
        final List<String> lines;

        path   = Paths.get(scoreFile);
        scores = new ArrayList<>();

        if (Files.notExists(path))
        {
            return scores;
        }

        try
        {
            lines = Files.readAllLines(path);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        for (final String line : lines)
        {
            final Score score;

            if (line == null || line.isBlank())
            {
                continue;
            }

            score = parseScoreLine(line.trim());

            if (score != null)
            {
                scores.add(score);
            }
        }

        return scores;
    }

    /**
     * Parses the score lines from file and creates a score Object.
     *
     * @param line a line of text containing a score
     * @return a new Score object
     */
    private static Score parseScoreLine(final String line)
    {
        final String[] parts;
        final int expectedParts;
        final int dateIndex;
        final int gamesIndex;
        final int firstIndex;
        final int secondIndex;
        final int incorrectIndex;

        expectedParts = 5;

        dateIndex      = 0;
        gamesIndex     = 1;
        firstIndex     = 2;
        secondIndex    = 3;
        incorrectIndex = 4;

        parts = line.split(",");

        if (parts.length != expectedParts)
        {
            return null;
        }

        final LocalDateTime dateTimePlayed;
        final int numGamesPlayed;
        final int numCorrectFirstAttempt;
        final int numCorrectSecondAttempt;
        final int numIncorrectSecondAttempt;

        dateTimePlayed            = LocalDateTime.parse(parts[dateIndex].trim());
        numGamesPlayed            = Integer.parseInt(parts[gamesIndex].trim());
        numCorrectFirstAttempt    = Integer.parseInt(parts[firstIndex].trim());
        numCorrectSecondAttempt   = Integer.parseInt(parts[secondIndex].trim());
        numIncorrectSecondAttempt = Integer.parseInt(parts[incorrectIndex].trim());

        return new Score(dateTimePlayed,
                         numGamesPlayed,
                         numCorrectFirstAttempt,
                         numCorrectSecondAttempt,
                         numIncorrectSecondAttempt);
    }


    /**
     * Getter for amount of time played
     *
     * @return the amount of time played in format []
     */
    public LocalDateTime getDateTimePlayed()
    {
        return dateTimePlayed;
    }

    /**
     * Getter for the number of games played
     *
     * @return an int representing the number of games a user has played on this device
     */
    public int getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    /**
     * Getter for the number of answers correct on the user's FIRST attempt
     *
     * @return an int representing the number FIRST attempt correct answers
     */
    public int getNumCorrectFirstAttempt()
    {
        return numCorrectFirstAttempt;
    }

    /**
     * Getter for the number of answers correct on the user's SECOND attempt
     *
     * @return an int representing the number SECOND attempt correct answers
     */
    public int getNumCorrectSecondAttempt()
    {
        return numCorrectSecondAttempt;
    }

    /**
     * Getter for the number of incorrect answers
     *
     * @return an int representing the number of INCORRECT answers
     */
    public int getNumIncorrectTwoAttempts()
    {
        return numIncorrectTwoAttempts;
    }

    /**
     * Increase number of games played by 1
     */
    public void incrementNumGamesPlayed()
    {
        numGamesPlayed++;
    }

    /**
     * Increase number of CORRECT FIRST attempt answers by 1
     */
    public void incrementNumCorrectFirstAttempt()
    {
        numCorrectFirstAttempt++;
    }

    /**
     * Increase number of CORRECT second attempt answers by 1
     */
    public void incrementNumCorrectSecondAttempt()
    {
        numCorrectSecondAttempt++;
    }

    /**
     * Increase number of INCORRECT second attempt answers by 1
     */
    public void incrementNumIncorrectTwoAttempts()
    {
        numIncorrectTwoAttempts++;
    }

    /**
     * Getter for the score of the current game.
     * {@value #CORRECT_FIRST_GUESS_PTS} points for FIRST attempt correct
     * {@value #CORRECT_SECOND_GUESS_PTS} points for SECOND attempt correct
     * {@value #INCORRECT_GUESS_PTS} points after {@value #MAX_GUESSES} incorrect
     * guesses.
     *
     * @return the points total based on correct and incorrect answers.
     */
    public int getScore()
    {
        final int gameScore;
        final int firstAttemptScore;
        final int secondAttemptScore;
        final int incorrectAttemptScore;

        firstAttemptScore     = numCorrectFirstAttempt * CORRECT_FIRST_GUESS_PTS;
        secondAttemptScore    = numCorrectSecondAttempt * CORRECT_SECOND_GUESS_PTS;
        incorrectAttemptScore = NONE;

        gameScore = firstAttemptScore +
                    secondAttemptScore +
                    incorrectAttemptScore;

        return gameScore;
    }

    /**
     * Returns instance data as a sentence
     *
     * @return a string sentence of instance data
     */
    @Override
    public String toString()
    {
        final StringBuilder sb;
        final int gameScore;

        sb = new StringBuilder();

        gameScore = getScore();

        sb.append("Date and Time: ");
        sb.append(dateTimePlayed.format(FORMATTER));
        sb.append("\n");

        sb.append("Games Played: ");
        sb.append(numGamesPlayed);
        sb.append("\n");

        sb.append("Correct First Attempts: ");
        sb.append(numCorrectFirstAttempt);
        sb.append("\n");

        sb.append("Correct Second Attempts: ");
        sb.append(numCorrectSecondAttempt);
        sb.append("\n");

        sb.append("Incorrect Attempts: ");
        sb.append(numIncorrectTwoAttempts);
        sb.append("\n");

        sb.append("Score: ");
        sb.append(gameScore);
        sb.append(" points\n");

        return sb.toString();
    }
}
