package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;

public class DiscardCardFromDeckEffect extends AbstractEffect {
    private PhysicalCard _card;

    public DiscardCardFromDeckEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone() == Zone.DECK;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard " + GameUtils.getCardLink(_card) + " from deck";
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(GameUtils.getCardLink(_card) + " gets discarded from deck");
            gameState.removeCardsFromZone(Collections.singleton(_card));
            gameState.addCardToZone(_card, Zone.DISCARD);
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
