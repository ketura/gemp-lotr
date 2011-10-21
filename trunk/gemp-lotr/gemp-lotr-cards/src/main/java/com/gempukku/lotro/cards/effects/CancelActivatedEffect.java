package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

public class CancelActivatedEffect extends AbstractEffect {
    private PhysicalCard _source;
    private ActivateCardResult _effect;

    public CancelActivatedEffect(PhysicalCard source, ActivateCardResult effect) {
        _source = source;
        _effect = effect;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return !_effect.isEffectCancelled();
    }

    @Override
    public String getText(LotroGame game) {
        return "Cancel effect of " + GameUtils.getCardLink(_effect.getSource());
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " cancels effect - " + GameUtils.getCardLink(_effect.getSource()));
            _effect.cancelEffect();
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
