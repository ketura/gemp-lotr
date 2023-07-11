package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;

public class RemoveCharacterFromSkirmishEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final PhysicalCard _toRemove;

    public RemoveCharacterFromSkirmishEffect(PhysicalCard source, PhysicalCard toRemove) {
        _source = source;
        _toRemove = toRemove;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.inSkirmish.accepts(game, _toRemove);
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().removeFromSkirmish(_toRemove);
        }
        return new FullEffectResult(false);
    }
}
