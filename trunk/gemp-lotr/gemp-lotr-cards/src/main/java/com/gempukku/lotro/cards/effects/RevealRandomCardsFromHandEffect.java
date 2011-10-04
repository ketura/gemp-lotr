package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public abstract class RevealRandomCardsFromHandEffect extends AbstractEffect {
    private String _playerId;
    private int _count;

    protected RevealRandomCardsFromHandEffect(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        List<PhysicalCard> randomCards = GameUtils.getRandomCards(game.getGameState().getHand(_playerId), _count);
        cardsRevealed(randomCards);
        return new FullEffectResult(null, randomCards.size() == _count, randomCards.size() == _count);
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getHand(_playerId).size() >= _count;
    }

    protected abstract void cardsRevealed(List<PhysicalCard> revealedCards);
}
