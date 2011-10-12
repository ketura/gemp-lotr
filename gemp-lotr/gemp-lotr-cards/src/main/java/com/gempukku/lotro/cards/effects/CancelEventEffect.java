package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.PlayEventEffect;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class CancelEventEffect extends AbstractEffect {
    private PhysicalCard _source;
    private PlayEventEffect _effect;

    public CancelEventEffect(PhysicalCard source, PlayEventEffect effect) {
        _source = source;
        _effect = effect;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return !_effect.isCancelled();
    }

    @Override
    public String getText(LotroGame game) {
        return "Cancel effect - " + _effect.getText(game);
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (!_effect.isCancelled()) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " cancels effect - " + _effect.getText(game));
            _effect.cancel();
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
