package ca.bcit.comp2522.numbergame;

import java.util.Arrays;
import java.util.random.RandomGenerator;

/**
 * Abstract base class for the Number Game logic.
 *
 * @author David Martinez
 * @version 1.0
 */
public abstract class AbstractNumberGame implements GridGame
{
    private static final int EMPTY_VALUE = 0;
    private static final int LOWER_BOUND = 1;
    private static final int UPPER_BOUND = 1001;

    protected static final int ROWS       = 4;
    protected static final int COLS       = 5;
    protected static final int CELL_COUNT = ROWS * COLS;

    protected final int[] gridValues;       // 0 means empty
    protected       int   currentNumber;            // next number to place
    protected       int   successfulPlacements;

    protected int gamesPlayed;
    protected int gamesWon;
    protected int gamesLost;
    protected int totalPlacements;

    protected final RandomGenerator random;

    /**
     * Constructor for AbstractNumberGame.
     */
    protected AbstractNumberGame()
    {
        gridValues = new int[CELL_COUNT];
        random     = RandomGenerator.getDefault();
    }

    /**
     * Starts a new game by resetting the grid and generating the first number.
     */
    @Override
    public void startNewGame()
    {
        Arrays.fill(gridValues, EMPTY_VALUE);
        successfulPlacements = EMPTY_VALUE;
        currentNumber        = nextRandomNumber();
        onNumberUpdated(currentNumber);
        onGridUpdated(gridValues);
    }

    /**
     * Generates the next random number between a lower bound and an upperbound inclusive.
     *
     * @return the generated random number
     */
    protected int nextRandomNumber()
    {
        return random.nextInt(LOWER_BOUND, UPPER_BOUND);
    }

    /**
     * Checks if there is a valid placement for the given value in the grid.
     *
     * @param value the value to check for valid placement
     * @return true if there is a valid placement, false otherwise
     */
    protected boolean hasValidPlacementFor(final int value)
    {
        for (int i = 0; i < CELL_COUNT; i++)
        {
            if (isValidPlacement(i, value))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if placing a value at a specific index is valid according to game rules.
     *
     * @param index the index to check
     * @param value the value to place
     * @return true if the placement is valid, false otherwise
     */
    protected boolean isValidPlacement(final int index,
                                       final int value)
    {
        if (gridValues[index] != EMPTY_VALUE)
        {
            return false;
        }

        int prevValue;
        int nextValue;

        prevValue = Integer.MIN_VALUE;
        nextValue = Integer.MAX_VALUE;

        // find previous filled cell
        for (int i = index - 1; i >= EMPTY_VALUE; i--)
        {
            if (gridValues[i] != EMPTY_VALUE)
            {
                prevValue = gridValues[i];
                break;
            }
        }

        // find next filled cell
        for (int i = index + 1; i < CELL_COUNT; i++)
        {
            if (gridValues[i] != EMPTY_VALUE)
            {
                nextValue = gridValues[i];
                break;
            }
        }

        return prevValue <= value && value <= nextValue;
    }

    /**
     * Records the end of a game, updating statistics based on whether the player won or lost.
     *
     * @param won true if the player won, false if they lost
     */
    protected void recordGameEnd(final boolean won)
    {
        gamesPlayed++;
        if (won)
        {
            gamesWon++;
        }
        else
        {
            gamesLost++;
        }
        totalPlacements += successfulPlacements;
    }

    /**
     * Returns a summary of the player's statistics.
     *
     * @return a formatted string summarizing the player's game statistics
     */
    public String getStatsSummary()
    {
        double average;

        if (gamesPlayed == EMPTY_VALUE)
        {
            average = EMPTY_VALUE;
        }
        else
        {
            average = (double) totalPlacements / gamesPlayed;
        }

        final StringBuilder sb;
        sb = new StringBuilder();

        sb.append("You won ");
        sb.append(gamesWon);
        sb.append(" out of ");
        sb.append(gamesPlayed);
        sb.append(" games and lost ");
        sb.append(gamesLost);
        sb.append(" out of ");
        sb.append(gamesPlayed);
        sb.append(" games, with ");
        sb.append(totalPlacements);
        sb.append(" successful placements, an average of ");
        sb.append(String.format("%.2f", average));
        sb.append(" per game.");

        return sb.toString();
    }


    // Hooks that the concrete GUI class must implement
    protected abstract void onGridUpdated(final int[] gridValues);

    protected abstract void onNumberUpdated(final int currentNumber);

    protected abstract void onGameOver(final boolean won,
                                       final int impossibleNumber);
}
