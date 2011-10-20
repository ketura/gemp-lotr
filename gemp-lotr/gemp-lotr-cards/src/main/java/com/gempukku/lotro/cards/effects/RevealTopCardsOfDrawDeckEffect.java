package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

public abstract class RevealTopCardsOfDrawDeckEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerId;
    private int _count;

    public RevealTopCardsOfDrawDeckEffect(PhysicalCard source, String playerId, int count) {
        _source = source;
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() >= _count;
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
        List<? extends PhysicalCard> deck = game.getGameState().getDeck(_playerId);
        int count = Math.min(deck.size(), _count);
        LinkedList<PhysicalCard> topCards = new LinkedList<PhysicalCard>(deck.subList(0, count));
        if (topCards.size() > 0)
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed cards from top of " + _playerId + " deck - " + getAppendedNames(topCards));
        cardsRevealed(topCards);
        return new FullEffectResult(null, topCards.size() == _count, topCards.size() == _count);
    }

    protected abstract void cardsRevealed(List<PhysicalCard> cards);
}
