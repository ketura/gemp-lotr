package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.DiscardUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StackCardFromPlayEffect extends AbstractEffect {
    private PhysicalCard _card;
    private PhysicalCard _stackOn;

    public StackCardFromPlayEffect(PhysicalCard card, PhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    public String getText(LotroGame game) {
        return "Stack " + GameUtils.getCardLink(_card) + " on " + GameUtils.getCardLink(_stackOn);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone().isInPlay() && _stackOn.getZone().isInPlay();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();

            Set<PhysicalCard> discardedFromPlayCards = new HashSet<PhysicalCard>();
            Set<PhysicalCard> toMoveToDiscardCards = new HashSet<PhysicalCard>();

            DiscardUtils.cardsToChangeZones(gameState, Collections.singleton(_card), discardedFromPlayCards, toMoveToDiscardCards);

            Set<PhysicalCard> removeFromPlay = new HashSet<PhysicalCard>(toMoveToDiscardCards);
            removeFromPlay.add(_card);

            gameState.removeCardsFromZone(_card.getOwner(), removeFromPlay);

            // And put them in new zones (attached and stacked to discard, the card gets stacked on)
            for (PhysicalCard attachedCard : toMoveToDiscardCards)
                gameState.addCardToZone(game, attachedCard, Zone.DISCARD);

            game.getGameState().sendMessage(GameUtils.getCardLink(_card) + " is stacked on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().stackCard(game, _card, _stackOn);

            // Send the result (attached cards get discarded)
            for (PhysicalCard discardedCard : discardedFromPlayCards)
                game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(discardedCard));

            return new FullEffectResult(true, true);
        }
        return new FullEffectResult(false, false);
    }
}
