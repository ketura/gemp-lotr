package com.gempukku.lotro.game.state;

import com.gempukku.lotro.game.PhysicalCard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Skirmish {
    private PhysicalCard _fellowshipCharacter;
    private List<PhysicalCard> _shadowCharacters;
    private boolean _cancelled;

    private Set<PhysicalCard> _removedFromSkirmish = new HashSet<PhysicalCard>();

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

    public void setFellowshipCharacter(PhysicalCard fellowshipCharacter) {
        _fellowshipCharacter = fellowshipCharacter;
    }

    public void addRemovedFromSkirmish(PhysicalCard loser) {
        _removedFromSkirmish.add(loser);
    }

    public Set<PhysicalCard> getRemovedFromSkirmish() {
        return _removedFromSkirmish;
    }

    public void cancel() {
        _cancelled = true;
    }

    public boolean isCancelled() {
        return _cancelled;
    }
}
