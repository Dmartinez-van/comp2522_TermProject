package ca.bcit.comp2522.mygame;

/**
 * Level 1 Health Upgrade decorator for Clicker that increases max health.
 *
 * @author David Martinez
 * @version 1.0
 */
public class L1HealthUpgrade extends ClickerDecorator
{
    private static final double FLAT_HEALTH_INCREASE = 20.0;

    /**
     * Constructs a ClickerDecorator that wraps another Clicker.
     *
     * @param innerClicker the Clicker to be decorated
     */
    public L1HealthUpgrade(final Clicker innerClicker)
    {
        super(innerClicker);
    }

    /**
     * Gets the maximum health of the Clicker.
     *
     * @return max health
     */
    @Override
    public double getMaxHealth()
    {
        final double newMaxHealth;
        newMaxHealth = innerClicker.getMaxHealth() + FLAT_HEALTH_INCREASE;

        return newMaxHealth;
    }

    /**
     * Gets the current health of the Clicker.
     *
     * @return current health
     */
    @Override
    public double getCurrentHealth()
    {
        final double originalMaxHealth;
        final double originalCurrentHealth;
        final double sealedCurrentHealth;

        originalMaxHealth     = innerClicker.getMaxHealth();
        originalCurrentHealth = innerClicker.getCurrentHealth();

        sealedCurrentHealth = originalCurrentHealth / originalMaxHealth * getMaxHealth();

        return sealedCurrentHealth;
    }
}
