package ca.bcit.comp2522.WordGame;

import java.util.Map;

/**
 * Word class
 *
 * @author David Martinez
 * @version 1.0
 */
public class World
{
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


}
