package ca.bcit.comp2522.wordgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Word class
 *
 * @author David Martinez
 * @version 1.0
 */
public class World
{
    private final static int NONE = 0;

    private final Map<String, Country> countriesMap;

    /**
     * Full constructor
     *
     * @param countriesMap this world's counties as a Map
     */
    public World(final Map<String, Country> countriesMap)
    {
        checkMap(countriesMap);
        this.countriesMap = countriesMap;
    }

    /*
    Check if map is null, invalid if true
    Throws new IllegalArgumentException
     */
    private void checkMap(final Map<String, Country> countries)
    {
        if (countries == null)
        {
            throw new IllegalArgumentException("countries cannot be null");
        }
    }

    /**
     * Get the countries map.
     *
     * @return the countries map
     */
    public Map<String, Country> getCountriesMap()
    {
        return countriesMap;
    }

    /**
     * Get a country by its name.
     *
     * @param countryName the name of the country to retrieve
     * @return the Country object corresponding to the given name, or null if not found
     */
    public Country getCountryByName(final String countryName)
    {
        return countriesMap.get(countryName);
    }

    /**
     * Get a country by its capital city name.
     *
     * @param capitalName the name of the capital city
     * @return the Country object corresponding to the given capital city name, or null if not found
     */
    public Country getCountryByCapital(final String capitalName)
    {
        for (final Country country : countriesMap.values())
        {
            if (country.getCapitalCityName().equalsIgnoreCase(capitalName))
            {
                return country;
            }
        }
        return null;
    }

    /**
     * Return a list of all countries in the world.
     *
     * @return a list of all countries
     */
    public List<Country> getAllCountries()
    {
        final List<Country> allCountries;
        allCountries = new ArrayList<>(countriesMap.values());

        return allCountries;
    }
}