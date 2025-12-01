package ca.bcit.comp2522.mygame;

/**
 * Starter Clicker implementation with initial attributes.
 *
 * @author David Martinez
 * @version 1.0
 */
public class StarterClicker implements Clicker
{
    private static final double INITIAL_DAMAGE_PER_CLICK       = 1.0;
    private static final double INITIAL_AUTO_DAMAGE_PER_SECOND = 0.0;
    private static final double INITIAL_CLICK_RATE_PER_SECOND  = 1.0;
    private static final double INITIAL_MAX_HEALTH             = 10.0;
    private static final double NO_DAMAGE                      = 0.0;
    private static final double MIN_HEALTH                     = 0.0;

    private final double damagePerClick;
    private final double autoDamagePerSecond;
    private final double clickRatePerSecond;

    private final double maxHealth;

    private double currentHealth;

    /**
     * Constructs a StarterClicker with initial attributes.
     */
    public StarterClicker()
    {
        this.damagePerClick      = INITIAL_DAMAGE_PER_CLICK;
        this.autoDamagePerSecond = INITIAL_AUTO_DAMAGE_PER_SECOND;
        this.clickRatePerSecond  = INITIAL_CLICK_RATE_PER_SECOND;
        this.maxHealth           = INITIAL_MAX_HEALTH;
        this.currentHealth       = maxHealth;
    }

    /**
     * Gets the amount of damage dealt per click.
     *
     * @return damage per click
     */
    @Override
    public double getDamagePerClick()
    {
        return damagePerClick;
    }

    /**
     * Gets the amount of automatic damage dealt per second.
     *
     * @return auto damage per second
     */
    @Override
    public double getAutoDamagePerSecond()
    {
        return autoDamagePerSecond;
    }

    /**
     * Gets the click rate per second.
     *
     * @return clicks per second
     */
    @Override
    public double getClickRatePerSecond()
    {
        return clickRatePerSecond;
    }

    /**
     * Performs a click action, dealing damage.
     */
    @Override
    public void doClick()
    {
        System.out.println("Click! Dealt " + damagePerClick + " damage.");
    }

    /**
     * Updates the Clicker's state over time.
     *
     * @param deltaSeconds time elapsed since last update in seconds
     */
    @Override
    public void tick(final double deltaSeconds)
    {
        if (autoDamagePerSecond <= NO_DAMAGE)
        {
            return;
        }

        final double damage;

        damage = autoDamagePerSecond * deltaSeconds;
        System.out.println("Auto-dealt " + damage + " damage over " + deltaSeconds + " seconds.");

    }

    /**
     * Gets the maximum health of the Clicker.
     *
     * @return maximum health
     */
    @Override
    public double getMaxHealth()
    {
        return maxHealth;
    }

    /**
     * Gets the current health of the Clicker.
     *
     * @return current health
     */
    @Override
    public double getCurrentHealth()
    {
        return currentHealth;
    }

    /**
     * Changes the current health of the Clicker by a specified amount.
     *
     * @param healthChangeAmount amount to change health by (positive or negative)
     */
    @Override
    public void changeHealth(final double healthChangeAmount)
    {
        currentHealth += healthChangeAmount;

        if (currentHealth > maxHealth)
        {
            currentHealth = maxHealth;
        }
        else if (currentHealth < MIN_HEALTH)
        {
            currentHealth = MIN_HEALTH;
        }
    }
}
