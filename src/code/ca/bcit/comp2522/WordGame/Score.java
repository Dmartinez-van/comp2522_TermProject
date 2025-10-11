package ca.bcit.comp2522.WordGame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private final static int MAX_GUESSES = 2;

    private final LocalDateTime dateTimePlayed;

    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectSecondAttempt;

    /**
     * Full constructor
     *
     * @param dateTimePlayed The amount of time played in game
     * @param numGamesPlayed The number of full games played
     * @param numCorrectFirstAttempt The number of correct answers on the
     *                               user's first attempt
     * @param numCorrectSecondAttempt The number of correct answers on the
     *                                user's second attempt
     * @param numIncorrectSecondAttempt The number of incorrect answers after
     *                                  the user attempts twice
     */
    Score (final LocalDateTime dateTimePlayed,
           final int numGamesPlayed,
           final int numCorrectFirstAttempt,
           final int numCorrectSecondAttempt,
           final int numIncorrectSecondAttempt)
    {
        //TODO Add validation methods here

        this.dateTimePlayed = dateTimePlayed;
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectSecondAttempt = numIncorrectSecondAttempt;
    }

    /**
     * Getter for amount of time played
     * TODO Finish the time format in javaDoc
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
    public int getNumIncorrectSecondAttempt()
    {
        return numIncorrectSecondAttempt;
    }

    /**
     * Increase number of games played by 1
     */
    public void incrementNumGamesPlayed()
    {
        numGamesPlayed++;
    }

    /**
     * Increase number of correct FIRST attempt answers by 1
     */
    public void incrementNumCorrectFirstAttempt()
    {
        numCorrectFirstAttempt++;
    }

    /**
     * Increase number of correct SECOND attempt answers by 1
     */
    public void incrementNumCorrectSecondAttempt()
    {
        numCorrectSecondAttempt++;
    }

    /**
     * Increase number of incorrect SECOND attempt answers by 1
     */
    public void incrementNumIncorrectSecondAttempt()
    {
        numIncorrectSecondAttempt++;
    }

    /**
     * Calculate the score of the current game.
     * {@value #CORRECT_FIRST_GUESS_PTS} points for FIRST attempt correct
     * {@value #CORRECT_SECOND_GUESS_PTS} points for SECOND attempt correct
     * {@value #INCORRECT_GUESS_PTS} points after {@value #MAX_GUESSES} incorrect
     * guesses.
     *
     * @return the points total based on correct and incorrect answers.
     */
    private int calculateScore()
    {
        final int gameScore;
        final int firstAttemptScore;
        final int secondAttemptScore;
        final int incorrectAttemptScore;

        firstAttemptScore     = numCorrectFirstAttempt    * CORRECT_FIRST_GUESS_PTS;
        secondAttemptScore    = numCorrectSecondAttempt   * CORRECT_SECOND_GUESS_PTS;
        incorrectAttemptScore = numIncorrectSecondAttempt * INCORRECT_GUESS_PTS;

        gameScore = firstAttemptScore +
                    secondAttemptScore +
                    incorrectAttemptScore;

        return gameScore;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb;
        final int gameScore;

        sb = new StringBuilder();

        gameScore = calculateScore();

        //TODO Will eventually be used to write to 'score.txt' file
        sb.append("Date and Time: ");
        sb.append(dateTimePlayed);
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
        sb.append(numIncorrectSecondAttempt);
        sb.append("\n");

        sb.append("Score: ");
        sb.append(gameScore);
        sb.append("\n");

        return sb.toString();
    }
}
