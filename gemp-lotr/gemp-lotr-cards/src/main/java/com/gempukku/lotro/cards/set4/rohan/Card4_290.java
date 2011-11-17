package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Fellowship: Add (2) and place a villager from hand beneath your draw deck
 * to heal a [ROHAN] ally.
 */
public class Card4_290 extends AbstractPermanent {
    public Card4_290() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Supplies of the Mark");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Keyword.VILLAGER).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            action.appendCost(
                    new ChooseCardsFromHandEffect(playerId, 1, 1, Keyword.VILLAGER) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards)
                                action.insertCost(
                                        new PutCardFromHandOnBottomOfDeckEffect(selectedCard));
                        }
                    });
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, CardType.ALLY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
