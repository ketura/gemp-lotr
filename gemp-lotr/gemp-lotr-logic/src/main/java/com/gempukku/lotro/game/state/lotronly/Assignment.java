package com.gempukku.lotro.game.state.lotronly;

import com.gempukku.lotro.cards.PhysicalCard;

import java.util.Set;

public class Assignment {
    private final PhysicalCard _fellowshipCharacter;
    private final Set<PhysicalCard> _shadowCharacters;

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
