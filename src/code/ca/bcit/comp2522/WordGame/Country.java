package ca.bcit.comp2522.WordGame;

/**
 * Country
 *
 * @author David Martinez
 * @version 1.0
 */
public class Country
{
    private final String name;
    private final String capitalCityName;

    private final int[]  facts;

    /**
     * Full constructor
     *
     * @param name the name of this country
     * @param capitalCityName the name of the capital city of this country
     * @param facts a list of facts about this country
     */
    public Country(final String name,
                   final String capitalCityName,
                   final int[] facts)
    {
        checkString(name);
        checkString(capitalCityName);
        checkFacts(facts);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    /*
    Checks for null and blankness, invalid if either is true
    Throws new IllegalArgumentException
     */
    private void checkString(final String s)
    {
        if (s == null || s.isBlank())
        {
            throw new IllegalArgumentException("Cannot have null or blank strings");
        }
    }

    /*
    Check for null list, invalid if null
    Throws new IllegalArgumentException
     */
    private void checkFacts(final int[] facts)
    {
        if (facts == null)
        {
            throw new IllegalArgumentException("Must instantiate with a non-null list");
        }
    }

    /**
     * Get the name of the country.
     *
     * @return the name of the country.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the name of the capital city.
     *
     * @return the name of the capital city.
     */
    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Get the facts of the country.
     *
     * @return the facts of the country.
     */
    public int[] getFacts()
    {
        return facts;
    }
}
