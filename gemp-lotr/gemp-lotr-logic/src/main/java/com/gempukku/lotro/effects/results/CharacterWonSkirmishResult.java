package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CharacterWonSkirmishResult extends EffectResult {
    private final LotroPhysicalCard _winner;
    private final Set<LotroPhysicalCard> _involving;
    private final SkirmishType _type;

    public enum SkirmishType {
        OVERWHELM, NORMAL
    }

    public CharacterWonSkirmishResult(SkirmishType type, LotroPhysicalCard winner, LotroPhysicalCard involving) {
        super(EffectResult.Type.CHARACTER_WON_SKIRMISH);
        _type = type;
        _winner = winner;
        if (involving == null)
            _involving = Collections.emptySet();
        else
            _involving = Collections.singleton(involving);
    }

    public CharacterWonSkirmishResult(SkirmishType type, LotroPhysicalCard winner, Set<LotroPhysicalCard> involving) {
        super(EffectResult.Type.CHARACTER_WON_SKIRMISH);
        _type = type;
        _winner = winner;
        _involving = new HashSet<>(involving);
    }

    public Set<LotroPhysicalCard> getInvolving() {
        return _involving;
    }

    public SkirmishType getSkirmishType() {
        return _type;
    }

    public LotroPhysicalCard getWinner() {
        return _winner;
    }
}
