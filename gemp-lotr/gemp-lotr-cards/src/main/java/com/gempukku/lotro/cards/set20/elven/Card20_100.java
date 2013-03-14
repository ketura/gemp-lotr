package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RevealCardFromTopOfDeckResult;

import java.util.LinkedList;
import java.util.List;

/**
 * 0
 * •Peering Forward
 * Elven	Condition • Support Area
 * Each time you reveal an [Elven] card from the top of your draw deck, make a minion strength -1 until the regroup phase.
 */
public class Card20_100 extends AbstractPermanent {
    public Card20_100() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Peering Forward", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.revealedCardsFromTopOfDeck(effectResult, self.getOwner())) {
            RevealCardFromTopOfDeckResult revealedCardsResult = (RevealCardFromTopOfDeckResult) effectResult;
            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
            for (PhysicalCard revealedElvenCard : Filters.filter(revealedCardsResult.getRevealedCards(), game.getGameState(), game.getModifiersQuerying(), Culture.ELVEN)) {
                final RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new ChooseActiveCardEffect(self, self.getOwner(), "Choose a minion for strength -1 penalty", CardType.MINION) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.appendEffect(
                                        new AddUntilStartOfPhaseModifierEffect(
                                                new StrengthModifier(self, card, -1), Phase.REGROUP));
                            }
                        });
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
