package com.gempukku.lotro.game.state;

import com.gempukku.lotro.game.PhysicalCard;

import java.util.Set;

public class Assignment {
    private PhysicalCard _fellowshipCharacter;
    private Set<PhysicalCard> _shadowCharacters;

    public Assignment(PhysicalCard fellowshipCharacter, Set<PhysicalCard> shadowCharacters) {
        _fellowshipCharacter = fellowshipCharacter;
        _shadowCharacters = shadowCharacters;
    }

    public PhysicalCard getFellowshipCharacter() {
        return _fellowshipCharacter;
    }

    public Set<PhysicalCard> getShadowCharacters() {
        return _shadowCharacters;
    }
}
