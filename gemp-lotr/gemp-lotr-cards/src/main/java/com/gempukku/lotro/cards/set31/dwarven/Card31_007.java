package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDeckEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Fellowship: Add (1) to take a card stacked here into hand. Regroup: Exert a
 * [DWARVEN] companion to stack a [DWARVEN] event from your draw deck on The Arkenstone (limit
 * once per phase).
 */
public class Card31_007 extends AbstractPermanent {
    public Card31_007() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.DWARVEN, "The Arkenstone", null, true);
	}

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
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
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            if (PlayConditions.checkPhaseLimit(game, self, 1)) {
                action.appendCost(
                        new IncrementPhaseLimitEffect(self, 1));
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF));
                action.appendEffect(
                        new ChooseCardsFromDeckEffect(playerId, 1, 1, CardType.EVENT, Culture.DWARVEN) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        for (PhysicalCard card : cards) {
                            game.getGameState().removeCardsFromZone(playerId, Collections.singleton(card));
                            game.getGameState().stackCard(game, card, self);
                        }
                    }
                });
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
