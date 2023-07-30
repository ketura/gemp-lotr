package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CharacterLostSkirmishResult extends EffectResult {
    private final LotroPhysicalCard _loser;
    private final Set<LotroPhysicalCard> _involving;
    private final SkirmishType _type;

    public enum SkirmishType {
        OVERWHELM, NORMAL
    }

    public CharacterLostSkirmishResult(SkirmishType type, LotroPhysicalCard loser, LotroPhysicalCard involving) {
        super(Type.CHARACTER_LOST_SKIRMISH);
        _type = type;
        _loser = loser;
        if (involving == null)
            _involving = Collections.emptySet();
        else
            _involving = Collections.singleton(involving);
    }

    public CharacterLostSkirmishResult(SkirmishType type, LotroPhysicalCard loser, Set<LotroPhysicalCard> involving) {
        super(Type.CHARACTER_LOST_SKIRMISH);
        _type = type;
        _loser = loser;
        _involving = new HashSet<>(involving);
    }

    public Set<LotroPhysicalCard> getInvolving() {
        return _involving;
    }

    public SkirmishType getSkirmishType() {
        return _type;
    }

    public LotroPhysicalCard getLoser() {
        return _loser;
    }
}