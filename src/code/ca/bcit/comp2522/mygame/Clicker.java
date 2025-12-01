package ca.bcit.comp2522.mygame;

/**
 * Interface representing a Clicker entity in the game.
 * @author David Martinez
 * @version 1.0
 */
public interface Clicker
{
    /**
     * Gets the amount of damage dealt per click.
     *
     * @return damage per click
     */
    double getDamagePerClick();

    /**
     * Gets the amount of automatic damage dealt per second.
     *
     * @return auto damage per second
     */
    double getAutoDamagePerSecond();

    /**
     * Gets the click rate per second.
     *
     * @return clicks per second
     */
    double getClickRatePerSecond();

    /**
     * Performs a click action, dealing damage.
     */
    void doClick();

    /**
     * Updates the Clicker's state over time.
     *
     * @param deltaSeconds time elapsed since last update in seconds
     */
    void tick(final double deltaSeconds);

    /**
     * Gets the maximum health of the Clicker.
     *
     * @return maximum health
     */
    double getMaxHealth();

    /**
     * Gets the current health of the Clicker.
     *
     * @return current health
     */
    double getCurrentHealth();

    /**
     * Changes the current health of the Clicker by a specified amount.
     *
     * @param amount amount to change health by (positive or negative)
     */
    void changeHealth(final double amount);
}
