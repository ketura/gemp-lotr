package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;
import com.gempukku.lotro.game.timing.results.PlayEventResult;

public class CancelEventEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final PlayEventResult _effect;

    public CancelEventEffect(PhysicalCard source, PlayEventResult effectResult) {
        _source = source;
        _effect = effectResult;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return !_effect.isEventCancelled();
    }

    @Override
    public String getText(DefaultGame game) {
        return "Cancel effect - " + GameUtils.getFullName(_effect.getPlayedCard());
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " cancels effect - " + GameUtils.getCardLink(_effect.getPlayedCard()));
            _effect.cancelEvent();
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
