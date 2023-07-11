package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;

import java.util.Arrays;

public class PutCardFromHandOnBottomOfDeckEffect extends AbstractEffect {
    private final PhysicalCard[] physicalCards;
    private final boolean _reveal;

    public PutCardFromHandOnBottomOfDeckEffect(boolean reveal, PhysicalCard... physicalCard) {
        physicalCards = physicalCard;
        _reveal = reveal;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        for (PhysicalCard card : physicalCards) {
            if (card.getZone() != Zone.HAND)
                return false;
        }

        return true;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Put card from hand on bottom of deck";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            gameState.removeCardsFromZone(physicalCards[0].getOwner(), Arrays.asList(physicalCards));
            for (PhysicalCard physicalCard : physicalCards) {
                if(_reveal) {
                    gameState.sendMessage(physicalCard.getOwner() + " puts " + GameUtils.getCardLink(physicalCard) + " from from hand on bottom of their deck");
                }
                else {
                    gameState.sendMessage(physicalCard.getOwner() + " puts a card from hand on bottom of their deck");
                }
                gameState.putCardOnBottomOfDeck(physicalCard);
            }

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
