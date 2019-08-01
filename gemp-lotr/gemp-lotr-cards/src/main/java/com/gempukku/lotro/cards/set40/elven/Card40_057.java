package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RevealCardFromTopOfDeckResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: *Peering Forward
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Condition - Support Area
 * Card Number: 1C57
 * Game Text: Each time you reveal a card from the top of your draw deck, you may exert an Elf to make a minion
 * strength -1 until the regroup phase.
 */
public class Card40_057 extends AbstractPermanent {
    public Card40_057() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Peering Forward", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.revealedCardsFromTopOfDeck(effectResult, playerId)
                && PlayConditions.canExert(self, game, Race.ELF)) {
            RevealCardFromTopOfDeckResult reveleadResult = (RevealCardFromTopOfDeckResult) effectResult;
            int count = reveleadResult.getRevealedCards().size();

            List<OptionalTriggerAction> actions = new ArrayList<OptionalTriggerAction>(count);
            for (int i = 0; i < count; i++) {
                final OptionalTriggerAction action = new OptionalTriggerAction("perringForward-" + i, self);
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
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
