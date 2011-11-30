package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CharacterLostSkirmishResult extends EffectResult {
    private PhysicalCard _loser;
    private Set<PhysicalCard> _involving;
    private SkirmishType _type;

    public enum SkirmishType {
        OVERWHELM, NORMAL
    }

    public CharacterLostSkirmishResult(SkirmishType type, PhysicalCard loser, PhysicalCard involving) {
        super(Type.CHARACTER_LOST_SKIRMISH);
        _type = type;
        _loser = loser;
        if (involving == null)
            _involving = Collections.emptySet();
        else
            _involving = Collections.singleton(involving);
    }

    public CharacterLostSkirmishResult(SkirmishType type, PhysicalCard loser, Set<PhysicalCard> involving) {
        super(Type.CHARACTER_LOST_SKIRMISH);
        _type = type;
        _loser = loser;
        _involving = new HashSet<PhysicalCard>(involving);
    }

    public Set<PhysicalCard> getInvolving() {
        return _involving;
    }

    public SkirmishType getSkirmishType() {
        return _type;
    }

    public PhysicalCard getLoser() {
        return _loser;
    }
}