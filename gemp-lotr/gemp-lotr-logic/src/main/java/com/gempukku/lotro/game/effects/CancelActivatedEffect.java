package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.results.ActivateCardResult;

public class CancelActivatedEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final ActivateCardResult _effect;

    public CancelActivatedEffect(PhysicalCard source, ActivateCardResult effect) {
        _source = source;
        _effect = effect;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return !_effect.isEffectCancelled();
    }

    @Override
    public String getText(DefaultGame game) {
        return "Cancel effect of " + GameUtils.getFullName(_effect.getSource());
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " cancels effect - " + GameUtils.getCardLink(_effect.getSource()));
            _effect.cancelEffect();
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
