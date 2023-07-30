package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DiscardStackedCardsEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final Collection<? extends LotroPhysicalCard> _cards;

    public DiscardStackedCardsEffect(LotroPhysicalCard source, LotroPhysicalCard card) {
        this(source, Collections.singleton(card));
    }

    public DiscardStackedCardsEffect(LotroPhysicalCard source, Collection<? extends LotroPhysicalCard> cards) {
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
        for (LotroPhysicalCard card : _cards) {
            if (card.getZone() != Zone.STACKED)
                return false;
        }
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        GameState gameState = game.getGameState();

        Set<LotroPhysicalCard> toDiscard = new HashSet<>();
        for (LotroPhysicalCard card : _cards) {
            if (card.getZone() == Zone.STACKED)
                toDiscard.add(card);
        }

        if (toDiscard.size() > 0)
            gameState.sendMessage(getAppendedNames(toDiscard) + " " + GameUtils.be(toDiscard) + " discarded from being stacked");
        gameState.removeCardsFromZone(_source.getOwner(), toDiscard);
        for (LotroPhysicalCard card : toDiscard)
            gameState.addCardToZone(game, card, Zone.DISCARD);

        return new FullEffectResult(toDiscard.size() == _cards.size());
    }
}
