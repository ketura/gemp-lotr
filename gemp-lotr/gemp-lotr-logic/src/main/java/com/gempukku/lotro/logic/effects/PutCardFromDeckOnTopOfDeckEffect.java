package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.Collections;

public class PutCardFromDeckOnTopOfDeckEffect extends AbstractEffect {
    private final String _performingPlayer;
    private final PhysicalCard _physicalCard;
    private final boolean _reveal;

    public PutCardFromDeckOnTopOfDeckEffect(String player, PhysicalCard physicalCard, boolean reveal) {
        _physicalCard = physicalCard;
        _performingPlayer = player;
        _reveal = reveal;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone() == Zone.DECK;
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getFullName(_physicalCard) + " on the top of draw deck";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            if(_reveal) {
                gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " on top of the deck");
            }
            else {
                gameState.sendMessage(_physicalCard.getOwner() + " puts a card on top of the deck");
            }
            gameState.removeCardsFromZone(_performingPlayer, Collections.singleton(_physicalCard));
            gameState.putCardOnTopOfDeck(_physicalCard);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
