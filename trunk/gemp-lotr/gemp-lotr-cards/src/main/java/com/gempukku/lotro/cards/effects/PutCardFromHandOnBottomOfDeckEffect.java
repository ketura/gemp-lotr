package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;

public class PutCardFromHandOnBottomOfDeckEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;

    public PutCardFromHandOnBottomOfDeckEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone() == Zone.HAND;
    }

    @Override
    public String getText(LotroGame game) {
        return "Put card from hand on bottom of deck";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_physicalCard.getOwner() + " puts a card from hand on bottom of his or her deck");
            gameState.removeCardsFromZone(_physicalCard.getOwner(), Collections.singleton(_physicalCard));
            gameState.putCardOnBottomOfDeck(_physicalCard);

            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
