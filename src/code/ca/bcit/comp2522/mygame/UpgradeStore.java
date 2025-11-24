package ca.bcit.comp2522.mygame;

import ca.bcit.comp2522.mygame.upgrades.Upgrade;

import java.util.Iterator;
import java.util.List;

public class UpgradeStore implements Iterable<Upgrade>
{
    private final List<Upgrade> upgrades;

    public UpgradeStore(List<Upgrade> upgrades)
    {
        this.upgrades = upgrades;
    }

    @Override
    public Iterator<Upgrade> iterator()
    {
        return upgrades.iterator();
    }
}

