package ca.bcit.comp2522.mygame;

import ca.bcit.comp2522.mygame.model.Clicker;

/**
 * Represents an upgrade that can be applied to a Clicker.
 *
 * @author David Martinez
 * @version 1.0
 */
@FunctionalInterface
public interface ClickerUpgrade
{
    /**
     * Applies this upgrade to the given base Clicker.
     *
     * @param baseClicker the Clicker to upgrade
     * @return a new Clicker instance with the upgrade applied
     */
    Clicker apply(Clicker baseClicker);
}
