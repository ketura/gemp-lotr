package com.gempukku.lotro.cards.set10.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Exert your Wizard and choose a number. Make each minion with that twilight cost strength -2 until
 * the regroup phase.
 */
public class Card10_016 extends AbstractEvent {
    public Card10_016() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Gathering Wind", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Race.WIZARD));
        action.appendEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new IntegerAwaitingDecision(1, "Choose a number", 0) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int twilightCost = getValidatedResult(result);
                                action.appendEffect(
                                        new AddUntilStartOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.and(CardType.MINION, Filters.printedTwilightCost(twilightCost)), -2), Phase.REGROUP));
                            }
                        }));
        return action;
    }
}
