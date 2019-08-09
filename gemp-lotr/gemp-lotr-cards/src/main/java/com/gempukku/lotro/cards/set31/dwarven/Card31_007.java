package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Smaug's twilight cost is -4. Fellowship: Add (1) to take a card stacked here into hand.
 * Maneuver: Stack a [DWARVEN] card from hand on The Arkenstone.
 */
public class Card31_007 extends AbstractPermanent {
    public Card31_007() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.DWARVEN, Zone.SUPPORT, "The Arkenstone", null, true);
	}
	
    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.name("Smaug"), -4);
	}

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(self);
            if (stackedCards.size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new AddTwilightEffect(self, 1));
                action.appendEffect(
                        new ChooseArbitraryCardsEffect(playerId, "Choose card", stackedCards, 1, 1) {
                            @Override
                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                for (PhysicalCard selectedCard : selectedCards) {
                                    action.appendEffect(
                                            new PutCardFromStackedIntoHandEffect(selectedCard));
                                }
                            }
                        });
                return Collections.singletonList(action);
            }
        }
		
		if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)) {
			if (Filters.filter(game.getGameState().getHand(playerId), game, Culture.DWARVEN).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendEffect(
                        new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Culture.DWARVEN));
                return Collections.singletonList(action);
            }
		}
		return null;
    }
}