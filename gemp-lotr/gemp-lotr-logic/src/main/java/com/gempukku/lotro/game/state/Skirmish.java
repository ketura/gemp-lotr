package com.gempukku.lotro.game.state;

import com.gempukku.lotro.game.PhysicalCard;

import java.util.HashSet;
import java.util.Set;

public class Skirmish {
    private PhysicalCard _fellowshipCharacter;
    private Set<PhysicalCard> _shadowCharacters;
    private boolean _cancelled;

    private Set<Integer> _removedFromSkirmish = new HashSet<Integer>();

    public Skirmish(PhysicalCard fellowshipCharacter, Set<PhysicalCard> shadowCharacters) {
        _fellowshipCharacter = fellowshipCharacter;
        _shadowCharacters = shadowCharacters;
    }

    public PhysicalCard getFellowshipCharacter() {
        return _fellowshipCharacter;
    }

    public Set<PhysicalCard> getShadowCharacters() {
        return _shadowCharacters;
    }

    public void setFellowshipCharacter(PhysicalCard fellowshipCharacter) {
        _fellowshipCharacter = fellowshipCharacter;
    }

    public void addRemovedFromSkirmish(PhysicalCard loser) {
        _removedFromSkirmish.add(loser.getCardId());
    }

    public Set<Integer> getRemovedFromSkirmish() {
        return _removedFromSkirmish;
    }

    public void cancel() {
        _cancelled = true;
    }

    public boolean isCancelled() {
        return _cancelled;
    }
}
