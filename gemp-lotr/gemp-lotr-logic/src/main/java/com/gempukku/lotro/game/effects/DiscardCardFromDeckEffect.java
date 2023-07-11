package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;

import java.util.Collections;

public class DiscardCardFromDeckEffect extends AbstractEffect {
    private final PhysicalCard _card;

    public DiscardCardFromDeckEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _card.getZone() == Zone.DECK;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Discard " + GameUtils.getFullName(_card) + " from deck";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.removeCardsFromZone(_card.getOwner(), Collections.singleton(_card));
            gameState.addCardToZone(game, _card, Zone.DISCARD);
            gameState.sendMessage(GameUtils.getCardLink(_card) + " gets discarded from deck");
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
