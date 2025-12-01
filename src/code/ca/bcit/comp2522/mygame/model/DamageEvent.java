package ca.bcit.comp2522.mygame.model;

/**
 * Represents a damage event in the game.
 *
 * @author David Martinez
 * @version 1.0
 */
public class DamageEvent
{
    private static final int MIN_SECONDS = 0;
    private static final int MIN_DAMAGE_AMOUNT = 0;
    private final long timestampSeconds;
    private final double damageAmount;

    /**
     * Constructs a DamageEvent with the specified timestamp and damage amount.
     *
     * @param timestampSeconds the time the damage occurred, in seconds
     * @param damageAmount     the amount of damage dealt
     */
    public DamageEvent(final long timestampSeconds,
                       final double damageAmount)
    {
        validateTimestampSeconds(timestampSeconds);
        validateDamageAmount(damageAmount);

        this.timestampSeconds = timestampSeconds;
        this.damageAmount     = damageAmount;
    }

    /**
     * Validates the damage amount.
     *
     * @param damageAmount the damage amount to validate
     */
    private void validateDamageAmount(final double damageAmount)
    {
        if (damageAmount < MIN_DAMAGE_AMOUNT)
        {
            throw new IllegalArgumentException("Damage amount cannot be negative.");
        }
    }

    /**
     * Validates the time stamp in nanoseconds.
     *
     * @param timestampSeconds the time stamp to validate
     */
    private void validateTimestampSeconds(final long timestampSeconds)
    {
        if (timestampSeconds < MIN_SECONDS)
        {
            throw new IllegalArgumentException("Timestamp cannot be negative.");
        }
    }

    /**
     * Gets the timestamp of the damage event in seconds.
     *
     * @return the timestamp in seconds
     */
    public double getTimestampSeconds()
    {
        return timestampSeconds;
    }

    /**
     * Gets the amount of damage dealt in the event.
     *
     * @return the damage amount
     */
    public double getDamageAmount()
    {
        return damageAmount;
    }
}
