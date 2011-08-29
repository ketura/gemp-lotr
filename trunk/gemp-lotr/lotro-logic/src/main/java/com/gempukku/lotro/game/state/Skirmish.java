package com.gempukku.lotro.game.state;

import com.gempukku.lotro.game.PhysicalCard;

import java.util.List;

public class Skirmish {
    private PhysicalCard _fellowshipCharacter;
    private List<PhysicalCard> _shadowCharacters;

    public Skirmish(PhysicalCard fellowshipCharacter, List<PhysicalCard> shadowCharacters) {
        _fellowshipCharacter = fellowshipCharacter;
        _shadowCharacters = shadowCharacters;
    }

    public PhysicalCard getFellowshipCharacter() {
        return _fellowshipCharacter;
    }

    public List<PhysicalCard> getShadowCharacters() {
        return _shadowCharacters;
    }
}
