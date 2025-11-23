package ca.bcit.comp2522.wordgame;

/**
 * Represents a country with its name, capital city, and interesting facts.
 *
 * @author David Martinez
 * @version 1.0
 */
public class Country
{
    private final String   countryName;
    private final String   capitalCityName;
    private final String[] facts;

    /**
     * Constructs a Country object with the specified name, capital city, and facts.
     *
     * @param name            the name of this country
     * @param capitalCityName the name of the capital city of this country
     * @param facts           a list of facts about this country
     */
    public Country(final String name,
                   final String capitalCityName,
                   final String[] facts)
    {
        checkString(name);
        checkString(capitalCityName);
        checkFacts(facts);

        this.countryName     = name;
        this.capitalCityName = capitalCityName;
        this.facts           = facts;
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
    private void checkFacts(final String[] facts)
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
    public String getCountryName()
    {
        return countryName;
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
     * Get a fact about the country by its index.
     *
     * @param factIndex the index of the fact to retrieve
     * @return the fact at the specified index
     */
    public String getFact(final int factIndex)
    {
        return facts[factIndex];
    }

    /**
     * Returns string sentence for the country
     *
     * @return a string sentence for the country
     */
    @Override
    public String toString()
    {
        final StringBuilder sb;
        sb = new StringBuilder();

        sb.append("Country: ");
        sb.append(countryName);
        sb.append(", Capital: ");
        sb.append(capitalCityName);

        return sb.toString();
    }
}
