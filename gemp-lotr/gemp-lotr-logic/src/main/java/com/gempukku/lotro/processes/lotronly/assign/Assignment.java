package com.gempukku.lotro.processes.lotronly.assign;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

import java.util.Set;

public class Assignment {
    private final LotroPhysicalCard _fellowshipCharacter;
    private final Set<LotroPhysicalCard> _shadowCharacters;

    public Assignment(LotroPhysicalCard fellowshipCharacter, Set<LotroPhysicalCard> shadowCharacters) {
        _fellowshipCharacter = fellowshipCharacter;
        _shadowCharacters = shadowCharacters;
    }

    public LotroPhysicalCard getFellowshipCharacter() {
        return _fellowshipCharacter;
    }

    public Set<LotroPhysicalCard> getShadowCharacters() {
        return _shadowCharacters;
    }
}
