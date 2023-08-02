package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.rules.GameUtils;

import java.util.Collections;

public class PutCardFromDeckOnBottomOfDeckEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final LotroPhysicalCard _physicalCard;

    private final boolean _reveal;

    public PutCardFromDeckOnBottomOfDeckEffect(LotroPhysicalCard source, LotroPhysicalCard physicalCard, boolean reveal) {
        _physicalCard = physicalCard;
        _source = source;
        _reveal = reveal;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _physicalCard.getZone() == Zone.DECK;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Put " + GameUtils.getFullName(_physicalCard) + " on the bottom of draw deck";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            if(_reveal) {
                gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from deck on the bottom of deck");
            }
            else {
                gameState.sendMessage(_physicalCard.getOwner() + " puts a card from deck on the bottom of deck");
            }

            gameState.removeCardsFromZone(_source.getOwner(), Collections.singleton(_physicalCard));
            gameState.putCardOnBottomOfDeck(_physicalCard);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
