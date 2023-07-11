package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DiscardStackedCardsEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final Collection<? extends PhysicalCard> _cards;

    public DiscardStackedCardsEffect(PhysicalCard source, PhysicalCard card) {
        this(source, Collections.singleton(card));
    }

    public DiscardStackedCardsEffect(PhysicalCard source, Collection<? extends PhysicalCard> cards) {
        _source = source;
        _cards = cards;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Discard stacked - " + getAppendedTextNames(_cards);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        for (PhysicalCard card : _cards) {
            if (card.getZone() != Zone.STACKED)
                return false;
        }
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        GameState gameState = game.getGameState();

        Set<PhysicalCard> toDiscard = new HashSet<>();
        for (PhysicalCard card : _cards) {
            if (card.getZone() == Zone.STACKED)
                toDiscard.add(card);
        }

        if (toDiscard.size() > 0)
            gameState.sendMessage(getAppendedNames(toDiscard) + " " + GameUtils.be(toDiscard) + " discarded from being stacked");
        gameState.removeCardsFromZone(_source.getOwner(), toDiscard);
        for (PhysicalCard card : toDiscard)
            gameState.addCardToZone(game, card, Zone.DISCARD);

        return new FullEffectResult(toDiscard.size() == _cards.size());
    }
}
