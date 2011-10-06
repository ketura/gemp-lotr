package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. When you play this condition, stack a card from your discard pile here.
 * Fellowship: Discard a Free Peoples card stacked here to heal a Dwarf.
 */
public class Card4_044 extends AbstractPermanent {
    public Card4_044() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Courtesy of My Hall", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseCardsFromDiscardEffect(self.getOwner(), 1, 1, Filters.any()) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                action.appendEffect(
                                        new StackCardFromDiscardEffect(card, self));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.filter(game.getGameState().getAttachedCards(self), game.getGameState(), game.getModifiersQuerying(), Filters.side(Side.FREE_PEOPLE)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.sameCard(self), Filters.side(Side.FREE_PEOPLE)));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Filters.race(Race.DWARF)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
