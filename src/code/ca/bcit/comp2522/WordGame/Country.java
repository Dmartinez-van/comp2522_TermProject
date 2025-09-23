package ca.bcit.comp2522.WordGame;

public class Country
{
    private final String name;
    private final String capitalCityName;

    /*
    0 -> fact 1,
    1 -> fact 2, etc
     */
    private final int[] facts;

    public Country(final String name,
                   final String capitalCityName,
                   final int[] facts)
    {
        checkName(name);
        checkCapitalCityName(capitalCityName);
        checkFacts(facts);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    private void checkName(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
    }

    private void checkCapitalCityName(final String capitalCityName)
    {
        if (capitalCityName == null || capitalCityName.isBlank())
        {
            throw new IllegalArgumentException("Capital city name cannot be null or blank");
        }
    }

    private void checkFacts(final int[] facts)
    {
        if (facts == null || facts.length == 0)
        {
            throw new IllegalArgumentException("Facts cannot be null or empty");
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
