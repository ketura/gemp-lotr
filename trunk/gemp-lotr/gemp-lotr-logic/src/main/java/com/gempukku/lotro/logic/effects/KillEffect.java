package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KillEffect extends AbstractSuccessfulEffect {
    private List<PhysicalCard> _cards;

    public KillEffect(List<PhysicalCard> cards) {
        _cards = cards;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.KILL;
    }

    public List<PhysicalCard> getCharactersToBeKilled() {
        return _cards;
    }

    @Override
    public String getText(LotroGame game) {
        List<PhysicalCard> cards = getCharactersToBeKilled();
        return "Kill - " + getAppendedTextNames(cards);
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();
        Set<PhysicalCard> killedCards = new HashSet<PhysicalCard>();

        for (PhysicalCard card : _cards) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(GameUtils.getCardLink(card) + " gets killed");
            gameState.stopAffecting(card);
            gameState.removeCardFromZone(card);
            if (card.getBlueprint().getSide() == Side.FREE_PEOPLE) {
                killedCards.add(card);
                gameState.addCardToZone(card, Zone.DEAD);
            } else {
                discardedCards.add(card);
                gameState.addCardToZone(card, Zone.DISCARD);
            }

            List<PhysicalCard> attachedCards = gameState.getAttachedCards(card);
            for (PhysicalCard attachedCard : attachedCards) {
                discardedCards.add(attachedCard);

                gameState.stopAffecting(attachedCard);
                gameState.removeCardFromZone(attachedCard);
                gameState.addCardToZone(attachedCard, Zone.DISCARD);
            }

            List<PhysicalCard> stackedCards = gameState.getStackedCards(card);
            for (PhysicalCard stackedCard : stackedCards) {
                gameState.removeCardFromZone(stackedCard);
                gameState.addCardToZone(stackedCard, Zone.DISCARD);
            }
        }

        if (killedCards.size() > 0 && discardedCards.size() > 0) {
            return new EffectResult[]{new KillResult(killedCards), new DiscardCardsFromPlayResult(discardedCards)};
        } else if (killedCards.size() > 0) {
            return new EffectResult[]{new KillResult(killedCards)};
        } else if (discardedCards.size() > 0) {
            return new EffectResult[]{new DiscardCardsFromPlayResult(discardedCards)};
        } else {
            return null;
        }
    }
}
