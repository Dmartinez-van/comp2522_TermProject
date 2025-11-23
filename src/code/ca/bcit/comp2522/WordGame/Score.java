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

/**
 * Represents a score with number of games played, correct and incorrect answers.
 *
 * @author David Martinez
 * @version 1.0
 */
final public class Score
{
    private static final int CORRECT_FIRST_GUESS_PTS  = 2;
    private static final int CORRECT_SECOND_GUESS_PTS = 1;
    private static final int NONE                     = 0;
    private static final int SINGLE                   = 1;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime dateTimePlayed;

    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectTwoAttempts;

    /**
     * Constructs a Score object with the specified parameters.
     *
     * @param dateTimePlayed          the date and time the game was played
     * @param numGamesPlayed          the number of games played
     * @param numCorrectFirstAttempt  the number of correct answers on the first attempt
     * @param numCorrectSecondAttempt the number of correct answers on the second attempt
     * @param numIncorrectTwoAttempts the number of incorrect answers after two attempts
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
        line = score.toString() + System.lineSeparator();

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
     * Reads scores from a specified file and returns a list of Score objects.
     *
     * @param scoreFile the input file name
     * @return a list of Score objects read from the file
     */
    public static List<Score> readScoresFromFile(final String scoreFile)
    {
        final Path path;
        final List<Score> scores;
        final List<String> lines;
        final List<String> currentBlock;

        path         = Paths.get(scoreFile);
        scores       = new ArrayList<>();
        currentBlock = new ArrayList<>();

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

        for (final String rawLine : lines)
        {
            final String line;
            line = rawLine;

            if (line.trim().isEmpty())
            {
                if (!currentBlock.isEmpty())
                {
                    final Score score;
                    score = parseScoreBlock(new ArrayList<>(currentBlock));

                    if (score != null)
                    {
                        scores.add(score);
                    }

                    currentBlock.clear();
                }
            }
            else
            {
                currentBlock.add(line);
            }
        }

        if (!currentBlock.isEmpty())
        {
            final Score score;
            score = parseScoreBlock(currentBlock);

            if (score != null)
            {
                scores.add(score);
            }
        }

        return scores;
    }


    /**
     * Parses a block of lines representing a score and returns a Score object.
     *
     * @param blockLines the lines representing a score block
     * @return a Score object parsed from the block, or null if parsing fails
     */
    private static Score parseScoreBlock(final List<String> blockLines)
    {
        if (blockLines.size() < 5)
        {
            return null;
        }

        final String dateLine;
        final String gamesLine;
        final String firstLine;
        final String secondLine;
        final String incorrectLine;

        dateLine      = blockLines.get(0).trim();
        gamesLine     = blockLines.get(1).trim();
        firstLine     = blockLines.get(2).trim();
        secondLine    = blockLines.get(3).trim();
        incorrectLine = blockLines.get(4).trim();

        // Parse Date and Time
        if (!dateLine.startsWith("Date and Time: "))
        {
            return null;
        }

        final String dateText;
        dateText = dateLine.substring("Date and Time: ".length()).trim();

        final DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        final LocalDateTime dateTimePlayed;
        dateTimePlayed = LocalDateTime.parse(dateText, formatter);

        // Parse Games Played
        final int numGamesPlayed;
        numGamesPlayed =
            Integer.parseInt(gamesLine.substring("Games Played: ".length()).trim());

        // Parse First Attempt count
        final int numCorrectFirstAttempt;
        numCorrectFirstAttempt =
            Integer.parseInt(firstLine.substring("Correct First Attempts: ".length()).trim());

        // Parse Second Attempt count
        final int numCorrectSecondAttempt;
        numCorrectSecondAttempt =
            Integer.parseInt(secondLine.substring("Correct Second Attempts: ".length()).trim());

        // Parse Incorrect Attempts
        final int numIncorrectTwoAttempts;
        numIncorrectTwoAttempts =
            Integer.parseInt(incorrectLine.substring("Incorrect Attempts: ".length()).trim());

        return new Score(dateTimePlayed,
                         numGamesPlayed,
                         numCorrectFirstAttempt,
                         numCorrectSecondAttempt,
                         numIncorrectTwoAttempts);
    }


    /**
     * Getter for date and time played
     *
     * @return the date and time the game was played
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
     * Calculates and returns the total score based on correct and incorrect answers.
     *
     * @return the total score as an integer
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
     * Formats the score line for display or storage.
     *
     * @param score the Score object to format
     * @return a formatted string representing the score details
     */
    private static String formatScoreLine(final Score score)
    {
        final int gamesPlayed;
        gamesPlayed = score.getNumGamesPlayed();

        final int correctFirst;
        correctFirst = score.getNumCorrectFirstAttempt();

        final int correctSecond;
        correctSecond = score.getNumCorrectSecondAttempt();

        final int incorrectTwoAttempts;
        incorrectTwoAttempts = score.getNumIncorrectTwoAttempts();

        final String gameWord;
        if (gamesPlayed == SINGLE)
        {
            gameWord = "word game";
        }
        else
        {
            gameWord = "word games";
        }

        final StringBuilder builder;
        builder = new StringBuilder();

        builder.append("- ");
        builder.append(gamesPlayed);
        builder.append(" ");
        builder.append(gameWord);
        builder.append(" played\n");

        builder.append("- ");
        builder.append(correctFirst);
        builder.append(" correct answers on the first attempt\n");

        builder.append("- ");
        builder.append(correctSecond);
        builder.append(" correct answers on the second attempt\n");

        builder.append("- ");
        builder.append(incorrectTwoAttempts);
        builder.append(" incorrect answers on two attempts each");

        return builder.toString();
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

        if (numGamesPlayed > SINGLE)
        {
            sb.append("Total Score: ");
            sb.append(gameScore);
            sb.append(" points\n");
        }
        else
        {
            sb.append("Score: ");
            sb.append(gameScore);
            sb.append(" points\n");
        }

        return sb.toString();
    }

    /**
     * Returns formatted string of correct answers.
     *
     * @return a formatted string of correct answers
     */
    public String getCorrectAnswers()
    {
        return formatScoreLine(this);
    }

    /**
     * Increments the number of games played by 1.
     */
    public void incrementGamesPlayed()
    {
        numGamesPlayed++;
    }

    /**
     * Increments the number of first correct answers by 1.
     */
    public void incrementFirstCorrectAnswers()
    {
        numCorrectFirstAttempt++;
    }

    /**
     * Increments the number of second correct answers by 1.
     */
    public void incrementSecondCorrectAnswers()
    {
        numCorrectSecondAttempt++;
    }

    /**
     * Increments the number of incorrect answers by 1.
     */
    public void incrementIncorrectAnswers()
    {
        numIncorrectTwoAttempts++;
    }
}
