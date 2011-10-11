package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. Plays to your support area. Each time the fellowship moves during the regroup phase, you may
 * discard 2 cards from hand to take an [ISENGARD] tracker from your discard pile into hand.
 */
public class Card4_213 extends AbstractPermanent {
    public Card4_213() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "What Did You Discover?");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                && game.getGameState().getCurrentPhase() == Phase.REGROUP
                && game.getGameState().getHand(playerId).size() >= 2) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, 2));
            action.appendEffect(
                    new ChooseCardsFromDiscardEffect(playerId, 1, 1, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER)) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                action.appendEffect(
                                        new PutCardFromDiscardIntoHandEffect(card));
                            }

                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
