package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RevealCardFromTopOfDeckResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 0
 * •Peering Forward
 * Condition • Support Area
 * Each time you reveal the top card of your draw deck, you may make a minion strength -1 until the regroup phase
 * (or -2 if you reveal an [Elven] card).
 * http://www.lotrtcg.org/coreset/elven/peeringforward(r2).jpg
 */
public class Card20_100 extends AbstractPermanent {
    public Card20_100() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Peering Forward", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.revealedCardsFromTopOfDeck(effectResult, self.getOwner())) {
            RevealCardFromTopOfDeckResult revealedCardsResult = (RevealCardFromTopOfDeckResult) effectResult;
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
            final Collection<PhysicalCard> revealedCards = revealedCardsResult.getRevealedCards();
            for (PhysicalCard revealedCard : revealedCards) {
                int penalty = revealedCard.getBlueprint().getCulture() == Culture.ELVEN ? -2 : -1;
                final OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.setText("Give minion "+penalty+" strength");
                action.setTriggerIdentifier(self.getCardId() + "-" + revealedCard.getCardId());
                action.appendEffect(
                        new ChooseActiveCardEffect(self, self.getOwner(), "Choose a minion for strength " + penalty + " penalty", CardType.MINION) {
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
