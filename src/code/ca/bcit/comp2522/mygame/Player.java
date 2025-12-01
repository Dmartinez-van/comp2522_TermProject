package ca.bcit.comp2522.mygame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the player in the auto clicker game.
 * Owns the Clicker and player statistics.
 *
 * @author David Martinez
 * @version 1.0
 */
public class Player
{
    private static final int STARTING_NUM_OF_UPGRADE_POINTS = 10;
    private static final int STARTING_NUMBER_OF_ENEMIES_KILLED = 0;
    private static final int KEY_NOT_FOUND = 0;

    private final Map<String, Integer> stats;

    private Clicker clicker;

    /**
     * Constructs a Player with default stats and a StarterClicker.
     */
    public Player()
    {
        stats   = new HashMap<>();
        clicker = new StarterClicker();

        initializeDefaultStats();
    }

    /**
     * Initializes default player statistics.
     */
    private void initializeDefaultStats()
    {
        stats.put("enemiesDefeated", STARTING_NUMBER_OF_ENEMIES_KILLED);
        stats.put("upgradePoints", STARTING_NUM_OF_UPGRADE_POINTS);
    }

    /**
     * Gets the player's Clicker.
     *
     * @return the Clicker
     */
    public Clicker getClicker()
    {
        return clicker;
    }

    /**
     * Sets the player's Clicker.
     *
     * @param clicker the Clicker to set
     */
    public void setClicker(final Clicker clicker)
    {
        this.clicker = clicker;
    }

    /**
     * Gets the value of a specific player statistic.
     *
     * @param key the statistic key
     * @return the statistic value, or 0 if not found
     */
    public int getStat(final String key)
    {
        final Integer value;

        value = stats.get(key);

        if (value == null)
        {
            return KEY_NOT_FOUND;
        }

        return value;
    }

    /**
     * Sets the value of a specific player statistic.
     *
     * @param key   the statistic key
     * @param value the statistic value to set
     */
    public void setStat(final String key,
                        final int value)
    {
        stats.put(key, value);
    }

    /**
     * Adds a delta to a specific player statistic.
     *
     * @param key   the statistic key
     * @param delta the amount to add
     */
    public void addToStat(final String key,
                          final int delta)
    {
        final int current;

        current = getStat(key);
        stats.put(key, current + delta);
    }

    /**
     * Returns an unmodifiable view of the stats map.
     * Useful if you want to display all stats in the UI.
     *
     * @return unmodifiable map of stats
     */
    public Map<String, Integer> getStatsSnapshot()
    {
        return Collections.unmodifiableMap(stats);
    }
}
