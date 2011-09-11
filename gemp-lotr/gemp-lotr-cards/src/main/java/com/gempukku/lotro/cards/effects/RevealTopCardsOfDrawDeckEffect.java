package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

public abstract class RevealTopCardsOfDrawDeckEffect extends UnrespondableEffect {
    private String _playerId;
    private int _count;

    public RevealTopCardsOfDrawDeckEffect(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public void playEffect(LotroGame game) {
        List<? extends PhysicalCard> deck = game.getGameState().getDeck(_playerId);
        int count = Math.min(deck.size(), _count);
        cardsRevealed(new LinkedList<PhysicalCard>(deck.subList(0, count)));
    }

    protected abstract void cardsRevealed(List<PhysicalCard> cards);
}
