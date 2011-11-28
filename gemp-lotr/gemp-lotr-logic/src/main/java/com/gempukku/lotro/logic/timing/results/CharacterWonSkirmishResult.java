package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.Set;

public class CharacterWonSkirmishResult extends EffectResult {
    private PhysicalCard _winner;
    private Set<PhysicalCard> _involving;
    private SkirmishType _type;

    public enum SkirmishType {
        OVERWHELM, NORMAL
    }

    public CharacterWonSkirmishResult(SkirmishType type, PhysicalCard winner, PhysicalCard involving) {
        super(EffectResult.Type.CHARACTER_WON_SKIRMISH);
        _type = type;
        _winner = winner;
        if (involving == null)
            _involving = Collections.emptySet();
        else
            _involving = Collections.singleton(involving);
    }

    public CharacterWonSkirmishResult(SkirmishType type, PhysicalCard winner, Set<PhysicalCard> involving) {
        super(EffectResult.Type.CHARACTER_WON_SKIRMISH);
        _type = type;
        _winner = winner;
        _involving = involving;
    }

    public Set<PhysicalCard> getInvolving() {
        return _involving;
    }

    public SkirmishType getSkirmishType() {
        return _type;
    }

    public PhysicalCard getWinner() {
        return _winner;
    }
}
