package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.RuleUtils;

/**
 * Title: Arrows of Light
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event - Archery
 * Card Number: 1U33
 * Game Text: To play, spot an Elf archer companion. Make the fellowship archery total -X to discard X shadow conditions.
 */
public class Card40_033 extends AbstractEvent {
    public Card40_033() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Arrows of Light", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF, Keyword.ARCHER, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        int archeryTotal = RuleUtils.calculateFellowshipArcheryTotal(game);
        if (archeryTotal > 0) {
            action.appendCost(
                    new PlayoutDecisionEffect(playerId, new IntegerAwaitingDecision(1, "Choose number to limit the archery by", 0, archeryTotal, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            int value = getValidatedResult(result);
                            action.appendCost(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -value)));
                            action.appendEffect(
                                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, value, value, Side.SHADOW, CardType.CONDITION));
                        }
                    }));
        }
        return action;
    }
}
