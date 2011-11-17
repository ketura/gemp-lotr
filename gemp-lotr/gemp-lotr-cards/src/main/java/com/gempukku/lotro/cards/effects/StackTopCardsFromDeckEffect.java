package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class StackTopCardsFromDeckEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerId;
    private int _count;
    private PhysicalCard _target;

    public StackTopCardsFromDeckEffect(PhysicalCard source, String playerId, int count, PhysicalCard target) {
        _source = source;
        _playerId = playerId;
        _count = count;
        _target = target;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _target.getZone().isInPlay() && game.getGameState().getDeck(_playerId).size() >= _count;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_target.getZone().isInPlay()) {
            int stacked = 0;
            for (int i = 0; i < _count; i++) {
                final PhysicalCard card = game.getGameState().removeTopDeckCard(_playerId);
                if (card != null) {
                    game.getGameState().stackCard(game, card, _target);
                    stacked++;
                }
            }
            return new FullEffectResult(null, stacked == _count, stacked == _count);
        }
        return new FullEffectResult(null, false, false);
    }
}
