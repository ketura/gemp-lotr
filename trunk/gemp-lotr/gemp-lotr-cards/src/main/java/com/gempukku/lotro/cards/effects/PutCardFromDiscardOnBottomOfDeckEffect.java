package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;

public class PutCardFromDiscardOnBottomOfDeckEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;

    public PutCardFromDiscardOnBottomOfDeckEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone() == Zone.DISCARD;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from discard on the bottom of deck");
            gameState.removeCardsFromZone(Collections.singleton(_physicalCard));
            gameState.putCardOnBottomOfDeck(_physicalCard);
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getCardLink(_physicalCard) + " from discard on bottom of deck";
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }
}
