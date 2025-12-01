package ca.bcit.comp2522.mygame;

/**
 * Level 1 Click Upgrade that adds damage per click.
 *
 * @author David Martinez
 * @version 1.0
 */
public class L1ClickUpgrade extends ClickerDecorator
{
    private static final double ADDED_DAMAGE_PER_CLICK = 2.0;

    /**
     * Constructs a ClickerDecorator that wraps another Clicker.
     *
     * @param innerClicker the Clicker to be decorated
     */
    public L1ClickUpgrade(final Clicker innerClicker)
    {
        super(innerClicker);
    }

    /**
     * Gets the amount of damage dealt per click.
     *
     * @return damage per click with upgrade applied
     */
    @Override
    public double getDamagePerClick()
    {
        final double upgradedDamagePerClick;
        upgradedDamagePerClick = innerClicker.getDamagePerClick() + ADDED_DAMAGE_PER_CLICK;

        return upgradedDamagePerClick;
    }
}
