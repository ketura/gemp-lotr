package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;

public class ReplaceInSkirmishEffect extends AbstractEffect {
    private PhysicalCard _replacedBy;
    private Filterable[] _replacingFilter;

    public ReplaceInSkirmishEffect(PhysicalCard replacedBy, Filterable... replacingFilter) {
        _replacedBy = replacedBy;
        _replacingFilter = replacingFilter;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.and(_replacingFilter, Filters.inSkirmish));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().replaceInSkirmish(_replacedBy);
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
