package ca.bcit.comp2522.numbergame;

/**
 * Interface for Grid-based games
 *
 * @author David Martinez
 * @version 1.0
 */
public interface GridGame
{
    /**
     * Starts a new game session
     */
    void startNewGame();

    /**
     * Handles a cell click at the given index
     *
     * @param index the index of the clicked cell
     */
    void handleCellClick(final int index);
}
