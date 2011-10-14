package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Tale. Plays to your support area. When you play this condition, reveal the top 6 cards of your draw deck
 * and stack them here.
 * Fellowship: Spot a Dwarf and discard the top card of your draw deck to take a Free Peoples card stacked here into
 * hand.
 */
public class Card4_046 extends AbstractPermanent {
    public Card4_046() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Ever My Heart Rises", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new StackTopCardsFromDeckEffect(self, self.getOwner(), 6, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF))
                && game.getGameState().getDeck(playerId).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(playerId));
            action.appendEffect(
                    new ChooseStackedCardsEffect(action, playerId, 1, 1, Filters.sameCard(self), Filters.side(Side.FREE_PEOPLE)) {
                        @Override
                        protected void cardsChosen(Collection<PhysicalCard> stackedCards) {
                            for (PhysicalCard stackedCard : stackedCards) {
                                action.appendEffect(
                                        new PutCardFromStackedIntoHandEffect(stackedCard));

                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
