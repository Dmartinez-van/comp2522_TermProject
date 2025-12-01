package ca.bcit.comp2522.mygame.model;

import ca.bcit.comp2522.mygame.Clicker;
import ca.bcit.comp2522.mygame.L1AutoClickUpgrade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the L1AutoClickUpgrade decorator.
 *
 * @author David Martinez
 * @version 1.0
 */
public class L1AutoClickUpgradeTest
{
    /**
     * A minimal stub Clicker so we can test the decorator.
     * Only the methods used in the test need real values.
     */
    private static class StubClicker implements Clicker
    {
        private final double autoDamagePerSecond;

        /**
         * Constructs a StubClicker with specified auto damage per second.
         *
         * @param autoDamagePerSecond the auto damage per second
         */
        StubClicker(final double autoDamagePerSecond)
        {
            this.autoDamagePerSecond = autoDamagePerSecond;
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
         * Gets the amount of damage dealt per click.
         *
         * @return damage per click
         */
        @Override
        public double getDamagePerClick()
        {
            return 0.0;
        }

        /**
         * Gets the click rate per second.
         *
         * @return click rate per second
         */
        @Override
        public double getClickRatePerSecond()
        {
            return 0.0;
        }

        /**
         * Performs a click action, dealing damage.
         */
        @Override
        public void doClick()
        {
            // not needed for this test
        }

        /**
         * Updates the Clicker's state over time.
         *
         * @param deltaSeconds time elapsed since last update in seconds
         */
        @Override
        public void tick(final double deltaSeconds)
        {
            // not needed for this test
        }

        /**
         * Gets the maximum health of the Clicker.
         *
         * @return maximum health
         */
        @Override
        public double getMaxHealth()
        {
            return 0.0;
        }

        /**
         * Gets the current health of the Clicker.
         *
         * @return current health
         */
        @Override
        public double getCurrentHealth()
        {
            return 0.0;
        }

        /**
         * Changes the current health of the Clicker by a specified amount.
         *
         * @param delta amount to change health by (positive or negative)
         */
        @Override
        public void changeHealth(final double delta)
        {
            // not needed for this test
        }
    }

    /**
     * Tests that L1AutoClickUpgrade correctly increases auto damage by +1.0.
     */
    @Test
    public void testAutoDamageIncrease()
    {
        final double baseAutoDamage;
        baseAutoDamage = 2.5;

        final StubClicker baseClicker;
        baseClicker = new StubClicker(baseAutoDamage);

        final Clicker upgradedClicker;
        upgradedClicker = new L1AutoClickUpgrade(baseClicker);

        final double expected;
        expected = baseAutoDamage + 1.0;

        final double actual;
        actual = upgradedClicker.getAutoDamagePerSecond();

        assertEquals(expected, actual, 0.0001);
    }

    /**
     * Tests that other methods are passed through unchanged.
     */
    @Test
    public void testPassThroughMethods()
    {
        final StubClicker baseClicker;
        baseClicker = new StubClicker(5.0);

        final Clicker upgradedClicker;
        upgradedClicker = new L1AutoClickUpgrade(baseClicker);

        final double damagePerClick;
        damagePerClick = upgradedClicker.getDamagePerClick();

        assertEquals(0.0, damagePerClick, 0.0001,
                     "Decorator should pass through getDamagePerClick");
    }
}
