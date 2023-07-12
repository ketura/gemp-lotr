package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;

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
    public boolean isPlayableInFull(DefaultGame game) {
        return _physicalCard.getZone() == Zone.DECK;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Put " + GameUtils.getFullName(_physicalCard) + " on the top of draw deck";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
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
