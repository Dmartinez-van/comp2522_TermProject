package ca.bcit.comp2522.mygame;

/**
 * Level 1 Auto Click Upgrade that adds automatic damage per second.
 *
 * @author David Martinez
 * @version 1.0
 */
public class L1AutoClickUpgrade extends ClickerDecorator
{
    private static final double ADDED_AUTO_DAMAGE_PER_SECOND = 1.0;

    /**
     * Constructs a ClickerDecorator that wraps another Clicker.
     *
     * @param innerClicker the Clicker to be decorated
     */
    public L1AutoClickUpgrade(final Clicker innerClicker)
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
    }
}
