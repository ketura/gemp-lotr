package com.gempukku.lotro.adventure;

import java.util.HashMap;
import java.util.Map;

public class DefaultAdventureLibrary implements AdventureLibrary {
    private final Adventure _defaultAdventure = new DefaultAdventure();
    private final Map<String, Adventure> _customAdventures = new HashMap<>();

    @Override
    public Adventure getAdventure(String adventureType) {
        final Adventure adventure = _customAdventures.get(adventureType);
        if (adventure != null)
            return adventure;
        return _defaultAdventure;
    }
}
