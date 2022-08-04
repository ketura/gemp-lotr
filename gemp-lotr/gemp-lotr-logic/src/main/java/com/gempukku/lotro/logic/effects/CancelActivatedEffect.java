package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

public class CancelActivatedEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final ActivateCardResult _effect;

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
        return "Cancel effect of " + GameUtils.getFullName(_effect.getSource());
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
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
