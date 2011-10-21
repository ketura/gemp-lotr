package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DiscardStackedCardsEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Collection<PhysicalCard> _cards;

    public DiscardStackedCardsEffect(PhysicalCard source, PhysicalCard card) {
        this(source, Collections.singleton(card));
    }

    public DiscardStackedCardsEffect(PhysicalCard source, Collection<PhysicalCard> cards) {
        _source = source;
        _cards = cards;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard stacked - " + getAppendedTextNames(_cards);
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        for (PhysicalCard card : _cards) {
            if (card.getZone() != Zone.STACKED)
                return false;
        }
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        GameState gameState = game.getGameState();

        Set<PhysicalCard> toDiscard = new HashSet<PhysicalCard>();
        for (PhysicalCard card : _cards) {
            if (card.getZone() == Zone.STACKED)
                toDiscard.add(card);
        }

        if (toDiscard.size() > 0)
            gameState.sendMessage(getAppendedNames(toDiscard) + " " + GameUtils.be(toDiscard) + " discarded from being stacked");
        gameState.removeCardsFromZone(toDiscard);
        for (PhysicalCard card : toDiscard)
            gameState.addCardToZone(game, card, Zone.DISCARD);

        return new FullEffectResult(null, toDiscard.size() == _cards.size(), toDiscard.size() == _cards.size());
    }
}
