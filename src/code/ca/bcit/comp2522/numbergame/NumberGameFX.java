package ca.bcit.comp2522.numbergame;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * JavaFX implementation of the Number Game logic.
 *
 * @author David Martinez
 * @version 1.0
 */
public class NumberGameFX extends AbstractNumberGame
{
    private Consumer<int[]>              gridUpdateConsumer;
    private IntConsumer                  numberUpdateConsumer;
    private BiConsumer<Boolean, Integer> gameOverConsumer;

    /**
     * Sets the UI update consumers.
     *
     * @param gridUpdateConsumer   consumer for grid updates
     * @param numberUpdateConsumer consumer for number updates
     * @param gameOverConsumer     consumer for game over events
     */
    public void setUi(final Consumer<int[]> gridUpdateConsumer,
                      final IntConsumer numberUpdateConsumer,
                      final BiConsumer<Boolean, Integer> gameOverConsumer)
    {
        this.gridUpdateConsumer   = gridUpdateConsumer;
        this.numberUpdateConsumer = numberUpdateConsumer;
        this.gameOverConsumer     = gameOverConsumer;
    }

    /**
     * Handles a cell click at the given index.
     *
     * @param index the index of the clicked cell
     */
    @Override
    public void handleCellClick(final int index)
    {
        if (!isValidPlacement(index, currentNumber))
        {
            return;
        }

        gridValues[index] = currentNumber;
        successfulPlacements++;
        onGridUpdated(gridValues);

        // check win
        if (successfulPlacements == CELL_COUNT)
        {
            final int impossibleNumber;
            final boolean won;

            won              = true;
            impossibleNumber = -1;
            recordGameEnd(won);
            onGameOver(won, impossibleNumber);

            return;
        }

        // generate next number and see if there is a valid spot
        currentNumber = nextRandomNumber();
        if (!hasValidPlacementFor(currentNumber))
        {
            final boolean won;

            won = false;
            recordGameEnd(won);
            onGameOver(won, currentNumber);

            return;
        }

        onNumberUpdated(currentNumber);
    }

    /**
     * Called when the grid is updated.
     *
     * @param gridValues the current grid values
     */
    @Override
    protected void onGridUpdated(final int[] gridValues)
    {
        if (gridUpdateConsumer != null)
        {
            gridUpdateConsumer.accept(gridValues);
        }
    }

    /**
     * Called when the current number is updated.
     *
     * @param currentNumber the current number to place
     */
    @Override
    protected void onNumberUpdated(final int currentNumber)
    {
        if (numberUpdateConsumer != null)
        {
            numberUpdateConsumer.accept(currentNumber);
        }
    }

    /**
     * Called when the game is over.
     *
     * @param won              true if the player won, false otherwise
     * @param impossibleNumber the number that could not be placed
     */
    @Override
    protected void onGameOver(final boolean won,
                              final int impossibleNumber)
    {
        if (gameOverConsumer != null)
        {
            gameOverConsumer.accept(won, impossibleNumber);
        }
    }
}
