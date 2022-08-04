package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PutCardFromPlayOnTopOfDeckEffect extends AbstractEffect {
    private final PhysicalCard _physicalCard;

    public PutCardFromPlayOnTopOfDeckEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone().isInPlay();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            Set<PhysicalCard> discardedCards = new HashSet<>();
            Set<PhysicalCard> toGoToDiscardCards = new HashSet<>();

            DiscardUtils.cardsToChangeZones(game, Collections.singleton(_physicalCard), discardedCards, toGoToDiscardCards);

            GameState gameState = game.getGameState();

            Set<PhysicalCard> removeFromPlay = new HashSet<>(toGoToDiscardCards);
            removeFromPlay.add(_physicalCard);

            gameState.removeCardsFromZone(_physicalCard.getOwner(), removeFromPlay);

            gameState.putCardOnTopOfDeck(_physicalCard);
            for (PhysicalCard discardedCard : discardedCards) {
                game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(null, null, discardedCard));
            }
            for (PhysicalCard toGoToDiscardCard : toGoToDiscardCards)
                gameState.addCardToZone(game, toGoToDiscardCard, Zone.DISCARD);

            gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from play on the top of deck");

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getFullName(_physicalCard) + " from play on top of deck";
    }

    @Override
    public Type getType() {
        return null;
    }
}
