package ca.bcit.comp2522.mygame;

/**
 * Decorator class for Clicker that allows for extending functionality.
 *
 * @author David Martinez
 * @version 1.0
 */
public abstract class ClickerDecorator implements Clicker
{
    protected final Clicker innerClicker;

    /**
     * Constructs a ClickerDecorator that wraps another Clicker.
     *
     * @param innerClicker the Clicker to be decorated
     */
    protected ClickerDecorator(final Clicker innerClicker)
    {
        validateInnerClicker(innerClicker);
        this.innerClicker = innerClicker;
    }

    /**
     * Validates the inner Clicker.
     *
     * @param innerClicker the Clicker to validate
     * @throws IllegalArgumentException if innerClicker is null
     */
    private void validateInnerClicker(final Clicker innerClicker)
    {
        if (innerClicker == null)
        {
            throw new IllegalArgumentException("Inner Clicker cannot be null.");
        }
    }

    /**
     * Gets the amount of damage dealt per click.
     *
     * @return damage per click
     */
    @Override
    public double getDamagePerClick()
    {
        return innerClicker.getDamagePerClick();
    }

    /**
     * Gets the amount of automatic damage dealt per second.
     *
     * @return auto damage per second
     */
    @Override
    public double getAutoDamagePerSecond()
    {
        return innerClicker.getAutoDamagePerSecond();
    }

    /**
     * Gets the click rate per second.
     *
     * @return clicks per second
     */
    @Override
    public double getClickRatePerSecond()
    {
        return innerClicker.getClickRatePerSecond();
    }

    /**
     * Performs a click action, dealing damage.
     */
    @Override
    public void doClick()
    {
        innerClicker.doClick();
    }

    /**
     * Updates the Clicker's state over time.
     *
     * @param deltaSeconds time elapsed since last update in seconds
     */
    @Override
    public void tick(final double deltaSeconds)
    {
        innerClicker.tick(deltaSeconds);
    }

    /**
     * Gets the maximum health of the Clicker.
     *
     * @return maximum health
     */
    @Override
    public double getMaxHealth()
    {
        return innerClicker.getMaxHealth();
    }

    /**
     * Gets the current health of the Clicker.
     *
     * @return current health
     */
    @Override
    public double getCurrentHealth()
    {
        return innerClicker.getCurrentHealth();
    }

    /**
     * Changes the current health of the Clicker by a specified amount.
     *
     * @param healthChangeAmount amount to change health by (positive or negative)
     */
    @Override
    public void changeHealth(final double healthChangeAmount)
    {
        innerClicker.changeHealth(healthChangeAmount);
    }
}
