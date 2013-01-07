package com.gempukku.lotro.cards.set18.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PutCardsFromDeckIntoHandDiscardRestEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Pipeweed. Each time you play a pipeweed card, you may spot Gandalf to reveal the top 3 cards of your draw
 * deck. Take all Free Peoples cards revealed into hand and discard the remaining cards.
 */
public class Card18_027 extends AbstractPermanent {
    public Card18_027() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Ship of Smoke", null, true);
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.owner(playerId), Keyword.PIPEWEED)
                && PlayConditions.canSpot(game, Filters.gandalf)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 3) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            action.appendEffect(
                                    new PutCardsFromDeckIntoHandDiscardRestEffect(action, self, playerId, revealedCards, Side.FREE_PEOPLE));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
