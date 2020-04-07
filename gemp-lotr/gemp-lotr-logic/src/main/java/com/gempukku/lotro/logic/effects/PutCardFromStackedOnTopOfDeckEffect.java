package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.Collections;

public class PutCardFromStackedOnTopOfDeckEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;

    public PutCardFromStackedOnTopOfDeckEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone() == Zone.STACKED;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from being stacked on the top of deck");
            gameState.removeCardsFromZone(_physicalCard.getOwner(), Collections.singleton(_physicalCard));
            gameState.putCardOnTopOfDeck(_physicalCard);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getFullName(_physicalCard) + " from being stacked on top of deck";
    }

    @Override
    public Type getType() {
        return null;
    }
}
