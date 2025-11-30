package ca.bcit.comp2522.mygame.model;

/**
 * Level 2 Auto Click Upgrade decorator for Clicker that increases automatic damage per second.
 *
 * @author David Martinez
 * @version 1.0
 */
public class L2AutoClickUpgrade extends ClickerDecorator
{
    private static final double ADDED_AUTO_DAMAGE_PER_SECOND = 3.0;

    /**
     * Constructs a ClickerDecorator that wraps another Clicker.
     *
     * @param innerClicker the Clicker to be decorated
     */
    public L2AutoClickUpgrade(final Clicker innerClicker)
    {
        super(innerClicker);
    }

    /**
     * Gets the amount of automatic damage dealt per second.
     *
     * @return auto damage per second
     */
    @Override
    public double getAutoDamagePerSecond()
    {
        final double newAutoDamagePerSecond;
        newAutoDamagePerSecond = innerClicker.getAutoDamagePerSecond() + ADDED_AUTO_DAMAGE_PER_SECOND;

        return newAutoDamagePerSecond;
    };

    /**
     * Updates the Clicker's state over time.
     *
     * @param deltaSeconds time elapsed since last update in seconds
     */
    @Override
    public void tick(final double deltaSeconds)
    {
        innerClicker.tick(deltaSeconds);

        final double extraDamage;

        extraDamage = ADDED_AUTO_DAMAGE_PER_SECOND * deltaSeconds;
//        System.out.println("L1AutoClickUpgrade adds " + extraDamage + " auto damage.");
    }
}
