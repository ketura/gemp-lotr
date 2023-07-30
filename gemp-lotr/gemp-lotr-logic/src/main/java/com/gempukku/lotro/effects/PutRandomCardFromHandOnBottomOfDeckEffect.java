package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.rules.GameUtils;

import java.util.Collections;
import java.util.List;

public class PutRandomCardFromHandOnBottomOfDeckEffect extends AbstractEffect {
    private final String _playerId;

    public PutRandomCardFromHandOnBottomOfDeckEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getHand(_playerId).size() > 0;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Put random card from hand on bottom of deck";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            final List<LotroPhysicalCard> randomCards = GameUtils.getRandomCards(gameState.getHand(_playerId), 1);
            for (LotroPhysicalCard randomCard : randomCards) {
                gameState.sendMessage(randomCard.getOwner() + " puts a card at random from hand on bottom of their deck");
                gameState.removeCardsFromZone(randomCard.getOwner(), Collections.singleton(randomCard));
                gameState.putCardOnBottomOfDeck(randomCard);
                putCardFromHandOnBottomOfDeckCallback(randomCard);
            }

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    protected void putCardFromHandOnBottomOfDeckCallback(LotroPhysicalCard card) {

    }
}
